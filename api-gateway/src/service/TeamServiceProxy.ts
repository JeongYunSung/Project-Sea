import fetch from 'node-fetch'
import Options from '../service/Options';

class TeamServiceProxy {

    private readonly teamService: string;

    constructor(options: Options) {
        this.teamService = `${options.baseUrl}/teams`
    }

    async acceptTeam(token: string, authorizeToken: string): Promise<any> {
        const response = await fetch(`${this.teamService}/join/accept`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({token:authorizeToken}),
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

    async rejectTeam(token: string, authorizeToken: string): Promise<any> {
        const response = await fetch(`${this.teamService}/join/reject`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({token:authorizeToken}),
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

    async quitTeam(token: string, id: number): Promise<any> {
        const response = await fetch(`${this.teamService}/quit/${id}`, {
            method: 'PUT',
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

    async joinTeam(token: string, id: number): Promise<any> {
        const response = await fetch(`${this.teamService}/join/${id}`, {
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

    async findTeamMembers(token: string, id: number) {
        const response = await fetch(`${this.teamService}/${id}`, {
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

export default TeamServiceProxy;