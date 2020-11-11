import Token from "./Token";

class TokenManager {

    private _tokens: Map<String, Token> = new Map();
    private _nullToken: Token = new Token('', '', '');

    setToken(ip: string, token: Token): void {
        this._tokens.set(ip, token);
    }

    getToken(ip: string): Token {
        return this._tokens.get(ip);
    }

    hasToken(ip: string): boolean {
        return this._tokens.has(ip);
    }

    removeToken(ip: string): boolean {
        return this._tokens.delete(ip);
    }

    getNullToken() {
        return this._nullToken;
    }
}

export default TokenManager;