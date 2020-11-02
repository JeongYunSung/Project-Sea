class Options {

    private _baseUrl: string;

    constructor(baseUrl: string) {
        this._baseUrl = baseUrl;
    }

    get baseUrl(): string {
        return this._baseUrl;
    }

    set baseUrl(newUrl: string) {
        this._baseUrl = newUrl;
    }
}

export default Options;