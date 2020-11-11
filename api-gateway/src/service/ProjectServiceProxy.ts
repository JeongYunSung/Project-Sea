import fetch from 'node-fetch'
import Options from '../service/Options';
import withQuery from "with-query";
import * as FormData from "form-data";
import * as fs from "fs";

class ProjectServiceProxy {

    private projectService: string;

    constructor(options: Options) {
        this.projectService = `${options.baseUrl}/projects`
    }

    async findProject(token: string, id: number) {
        const response = await fetch(`${this.projectService}/${id}`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        const result = await response.json();
        if(response.status == 200) {
            return result;
        }else if(response.status < 500 && response.status >= 400) {
            return new Error((Array.isArray(result)) ? JSON.stringify(result[0].defaultMessage) : JSON.stringify(result));
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async findMyProjects(token: string, page: number, size: number) {
        const response = await fetch(withQuery(`${this.projectService}/me`, {page, size}), {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        let result = await response.json();
        if(response.status == 200) {
            if(result._embedded != undefined) {
                result = {projects: result._embedded.projectSearchResponseList, page: result.page};
            }else {
                result = {page: result.page};
            }
            return result;
        }else if(response.status < 500 && response.status >= 400) {
            return new Error((Array.isArray(result)) ? JSON.stringify(result[0].defaultMessage) : JSON.stringify(result));
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async searchProject(subject: string, projectState: string, boardCategory: string, username: number, page: number, size: number) {
        const response = await fetch(withQuery(`${this.projectService}/search`, {subject, projectState, boardCategory, username, page, size}), {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        let result = await response.json();
        if(response.ok) {
            if(result._embedded != undefined) {
                result = {projects: result._embedded.projectSearchResponseList, page: result.page};
            }else {
                result = {page: result.page};
            }
            return result;
        }else if(response.status < 500 && response.status >= 400) {
            return new Error((Array.isArray(result)) ? JSON.stringify(result[0].defaultMessage) : JSON.stringify(result));
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async bestProject(minDate: string, maxDate: string, size: number, category: string) {
        const response = await fetch(withQuery(`${this.projectService}/best`, {minDate, maxDate, size, category}), {
            headers: {
                'Content-Type': 'application/json',
            }
        });
        let result = await response.json();
        if(response.ok) {
            return result;
        }else if(response.status < 500 && response.status >= 400) {
            return new Error((Array.isArray(result)) ? JSON.stringify(result[0].defaultMessage) : JSON.stringify(result));
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async createProject(token: string, subject: string, content: string, category: string, minSize: number, maxSize: number, open: boolean, lastDate: string, files: any): Promise<any> {
        const formData = new FormData();
        formData.append('subject', subject);
        formData.append('content', content);
        formData.append('category', category);
        formData.append('minSize', minSize);
        formData.append('maxSize', maxSize);
        formData.append('open', `${open}`);
        formData.append('lastDate', lastDate);
        if(Array.isArray(files)) {
            Array.from(files).forEach(k => formData.append('file', fs.createReadStream(k)));
        }

        const response = await fetch(`${this.projectService}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            body: formData,
        });
        const result = await response.json();
        if(response.ok) {
            return result;
        }else if(response.status < 500 && response.status >= 400) {
            return new Error((Array.isArray(result)) ? JSON.stringify(result[0].defaultMessage) : JSON.stringify(result));
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async reviseProject(token: string, id: number, subject: string, content: string, open: boolean, files: any): Promise<any> {
        const formData = new FormData();
        formData.append('subject', subject);
        formData.append('content', content);
        formData.append('open', `${open}`);
        if(Array.isArray(files)) {
            Array.from(files).forEach(k => formData.append('file', fs.createReadStream(k)));
        }
        const response = await fetch(`${this.projectService}/revise/${id}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            body: formData,
        });
        const result = await response.json();
        if(response.ok) {
            return result;
        }else if(response.status < 500 && response.status >= 400) {
            return new Error((Array.isArray(result)) ? JSON.stringify(result[0].defaultMessage) : JSON.stringify(result));
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async cancelProject(token: string, id: number): Promise<any> {
        const response = await fetch(`${this.projectService}/cancel/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        const result = await response.json();
        if(response.ok) {
            return result;
        }else if(response.status < 500 && response.status >= 400) {
            return new Error((Array.isArray(result)) ? JSON.stringify(result[0].defaultMessage) : JSON.stringify(result));
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }
}

export default ProjectServiceProxy;