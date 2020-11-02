import fetch from 'node-fetch'
import Options from '../service/Options';
import DataLoader from 'data-loader'

class ProjectServiceProxy {

    private projectService: string;

    constructor(options: Options) {
        this.projectService = `${options.baseUrl}/projects`
    }
}

export default ProjectServiceProxy;