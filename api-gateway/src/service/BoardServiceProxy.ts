import fetch from 'node-fetch'
import DataLoader from 'data-loader'

class BoardServiceProxy {

    private boardService: string;

    constructor(options) {
        this.boardService = `${options.baseUrl}/boards`
    }
}

export default BoardServiceProxy;