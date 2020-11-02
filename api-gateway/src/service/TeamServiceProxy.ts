import fetch from 'node-fetch'
import Options from '../service/Options';
import DataLoader from 'data-loader'

class TeamServiceProxy {

    private teamService: string;

    constructor(options: Options) {
        this.teamService = `${options.baseUrl}/teams`
    }
}

export default TeamServiceProxy;