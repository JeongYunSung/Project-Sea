import fetch from 'node-fetch'
import Options from '../service/Options';
import * as FormData from 'form-data';
import * as base64 from 'base-64';
import TokenManager from "./TokenManager";
import Token from "./Token";

const config = require('../config/my-config.json');

class MemberServiceProxy {

    private readonly oauthUrl: string;
    private readonly memberService: string;

    constructor(options: Options) {
        this.oauthUrl = `${options.baseUrl}/oauth/token`
        this.memberService = `${options.baseUrl}/members`;
    }

    //Get

    async findProfile(): Promise<any> {
        const response = await fetch(`${this.memberService}/profile/me`, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer '
            }
        });
        const result = await response.json();
        if(response.status == 200) {
            return result;
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async isUsername(username: string): Promise<any> {
        const response = await fetch(`${this.memberService}/is/usernames/${username}`, {
            headers: {'Content-Type': 'application/json'}
        });
        const result = await response.json();
        if(response.status == 200) {
            return result;
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async isNickname(nickname: string): Promise<any> {
        const response = await fetch(`${this.memberService}/is/nicknames/${nickname}`, {
            headers: {'Content-Type': 'application/json'}
        });
        const result = await response.json();
        if(response.status == 200) {
            return result;
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }

    //Post

    async signIn(username: string, password: string, scopes: string[], ip: string, tokens: TokenManager): Promise<any> {
        const formData = new FormData();
        formData.append('grant_type', 'password');
        formData.append('username', username);
        formData.append('password', password);
        formData.append('scope', scopes.join(' '))
        const response = await fetch(`${this.oauthUrl}`, {
            method: 'POST',
            headers: {
                'Authorization': 'Basic ' + base64.encode(config.client_id + ':' + config.client_secret),
                'x-forwarded-for': ip
            },
            body: formData
        });
        const result = await response.json();
        if(response.status == 200) {
            tokens.setToken(ip, new Token(result.access_token, result.refresh_token, result.scope));
            return result;
        }else if(response.status == 400) {
            return new Error(result.error_description);
        }else if(response.status == 401) {
            return new Error(result.error_description);
        }
        else {
            return new Error('Unexpected Http Status Code');
        }
    }

    async signUp(username: string, nickname: string, password: string): Promise<any> {
        const response = await fetch(`${this.memberService}/signup`, {
                method: 'POST',
                body: JSON.stringify({username, password, nickname}),
                headers: {'Content-Type': 'application/json'}
            });
        const result = await response.json();
        if(response.status == 400) {
            return new Error(result[0].defaultMessage);
        }else if(response.ok) {
            return result;
        }else {
            return new Error('Unexpected Http Status Code');
        }
    }
}

export default MemberServiceProxy;