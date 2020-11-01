import fetch from 'node-fetch'
import DataLoader from 'data-loader'

class TeamServiceProxy {

    private teamService: string;

    constructor(options) {
        this.teamService = `${options.baseUrl}/teams`
    }
}

export default TeamServiceProxy;