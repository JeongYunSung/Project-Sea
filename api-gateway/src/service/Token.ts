class Token {

    private _accessToken: string;
    private _refreshToken: string;
    private _scope: string;

    constructor(accessToken: string, refreshToken: string, scope: string) {
        this._accessToken = accessToken;
        this._refreshToken = refreshToken;
        this._scope = scope;
    }

    get accessToken(): string {
        return this._accessToken;
    }

    set accessToken(value: string) {
        this._accessToken = value;
    }

    get refreshToken(): string {
        return this._refreshToken;
    }

    set refreshToken(value: string) {
        this._refreshToken = value;
    }

    get scope(): string {
        return this._scope;
    }

    set scope(value: string) {
        this._scope = value;
    }
}

export default Token;