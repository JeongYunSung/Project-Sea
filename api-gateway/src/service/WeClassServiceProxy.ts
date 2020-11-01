import fetch from 'node-fetch'
import DataLoader from 'data-loader'

class WeClassServiceProxy {

    private weClassService: string;

    constructor(options) {
        this.weClassService = `${options.baseUrl}/classes`
    }
}

export default WeClassServiceProxy;