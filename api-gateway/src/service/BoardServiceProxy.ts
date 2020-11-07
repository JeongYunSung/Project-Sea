import fetch from 'node-fetch';
import * as fs from 'fs';
import * as FormData from 'form-data';
import Options from '../service/Options';
import withQuery from "with-query";
import DataLoader from 'data-loader';

const config = require('../config/my-config.json');

class BoardServiceProxy {

    private readonly boardService: string;

    constructor(options: Options) {
        this.boardService = `${options.baseUrl}/boards`
    }

    async findBoard(token: string, id: number) {
        const response = await fetch(`${this.boardService}/${id}`, {
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

    async searchBoard(token: string, writer: string, subject: string, category: string, recommendCount: number, page: number, size: number) {
        const response = await fetch(withQuery(`${this.boardService}`, {writer, subject, category, recommendCount, page, size}), {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        let result = await response.json();
        if(result._embedded != undefined) {
            result = {boards: result._embedded.boardSearchResponseList, page: result.page};
        }else {
            result = {page: result.page};
        }
        if(response.ok) {
            return result;
        }else if(response.status < 500 && response.status >= 400) {
            return new Error((Array.isArray(result)) ? JSON.stringify(result[0].defaultMessage) : JSON.stringify(result));
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async findComments(token: string, id: number, page: number, size: number) {
        const response = await fetch(withQuery(`${this.boardService}/${id}/comments`, {page, size}), {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        let result = await response.json();
        if(result._embedded != undefined) {
            result = {comments: result._embedded.commentResponseList, page: result.page};
        }else {
            result = {page: result.page};
        }
        if(response.ok) {
            return result;
        }else if(response.status < 500 && response.status >= 400) {
            return new Error((Array.isArray(result)) ? JSON.stringify(result[0].defaultMessage) : JSON.stringify(result));
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async createBoard(token: string, subject: string, content: string, category: string, files: any): Promise<any> {
        const formData = new FormData();
        formData.append('subject', subject);
        formData.append('content', content);
        formData.append('category', category);
        if(Array.isArray(files)) {
            Array.from(files).forEach(k => formData.append('file', fs.createReadStream(k)));
        }

        const response = await fetch(`${this.boardService}`, {
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

    async createComment(token: string, id: number, commentId: number, content: string): Promise<any> {
        const body = commentId != undefined ? {commentId, content} : {content};
        const response = await fetch(`${this.boardService}/${id}/comments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(body),
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

    async putBoard(token: string, id: number, subject: string, content: string): Promise<any> {
        const response = await fetch(`${this.boardService}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({subject, content}),
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

    async recommendBoard(token: string, id: number): Promise<any> {
        const response = await fetch(`${this.boardService}/${id}/recommend`, {
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

    async putComment(token: string, id: number, content: string): Promise<any> {
        const response = await fetch(`${this.boardService}/comments/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({content}),
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

    async deleteBoard(token: string, id: number): Promise<any> {
        const response = await fetch(`${this.boardService}/${id}`, {
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

    async deleteComment(token: string, id: number): Promise<any> {
        const response = await fetch(`${this.boardService}/comments/${id}`, {
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
}

export default BoardServiceProxy;