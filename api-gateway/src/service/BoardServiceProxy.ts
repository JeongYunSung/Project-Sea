import fetch from 'node-fetch';
import Options from '../service/Options';
import DataLoader from 'data-loader';

class BoardServiceProxy {

    private boardService: string;

    constructor(options: Options) {
        this.boardService = `${options.baseUrl}/boards`
    }
}

export default BoardServiceProxy;