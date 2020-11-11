import fetch from 'node-fetch'
import Options from '../service/Options';
import withQuery from "with-query";

class NotificationServiceProxy {

    private readonly notificationService: string;

    constructor(options: Options) {
        this.notificationService = `${options.baseUrl}/notifications`
    }

    async findMyNotifications(token: string, page: number, size: number) {
        const response = await fetch(withQuery(`${this.notificationService}/me`, {page, size}), {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        let result = await response.json();
        if(response.status == 200) {
            if(result._embedded != undefined) {
                result = {notifications: result._embedded.notificationBasicResponseList, page: result.page};
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

    async findById(token: string, id: number) {
        const response = await fetch(`${this.notificationService}/${id}`, {
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

export default NotificationServiceProxy;