import fetch from 'node-fetch'
import Options from '../service/Options';
import DataLoader from 'data-loader'

class WeClassServiceProxy {

    private weClassService: string;

    constructor(options: Options) {
        this.weClassService = `${options.baseUrl}/classes`
    }
}

export default WeClassServiceProxy;