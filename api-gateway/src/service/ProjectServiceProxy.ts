import fetch from 'node-fetch'
import DataLoader from 'data-loader'

class ProjectServiceProxy {

    private projectService: string;

    constructor(options) {
        this.projectService = `${options.baseUrl}/projects`
    }
}

export default ProjectServiceProxy;