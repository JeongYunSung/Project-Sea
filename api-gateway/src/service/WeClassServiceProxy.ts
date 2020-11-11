import fetch from 'node-fetch'
import Options from '../service/Options';
import withQuery from "with-query";

class WeClassServiceProxy {

    private readonly weClassService: string;

    constructor(options: Options) {
        this.weClassService = `${options.baseUrl}/classes`
    }

    async createReport(token: string, id: number, subject: string, content: string): Promise<any> {
        const response = await fetch(`${this.weClassService}/${id}/report`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({subject, content})
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

    async putReport(token: string, id: number, subject: string, content: string): Promise<any> {
        const response = await fetch(`${this.weClassService}/reports/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({subject, content})
        });
        if(response.ok) {
            return true;
        }else if(response.status < 500 && response.status >= 400) {
            const result = await response.json();
            return new Error((Array.isArray(result)) ? JSON.stringify(result[0].defaultMessage) : JSON.stringify(result));
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async deleteReport(token: string, id: number): Promise<any> {
        const response = await fetch(`${this.weClassService}/reports/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        if(response.ok) {
            return true;
        }else if(response.status < 500 && response.status >= 400) {
            const result = await response.json();
            return new Error((Array.isArray(result)) ? JSON.stringify(result[0].defaultMessage) : JSON.stringify(result));
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async findNotice(token: string, id: number) {
        const response = await fetch(`${this.weClassService}/${id}/notice`, {
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

    async findReports(token: string, id: number, page: number, size: number) {
        const response = await fetch(withQuery(`${this.weClassService}/${id}/reports`, {page, size}), {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        let result = await response.json();
        if(response.status == 200) {
            if(result._embedded != undefined) {
                result = {reports: result._embedded.weClassReportResponseList, page: result.page};
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

    async findReport(token: string, id: number) {
        const response = await fetch(`${this.weClassService}/reports/${id}`, {
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
}

export default WeClassServiceProxy;