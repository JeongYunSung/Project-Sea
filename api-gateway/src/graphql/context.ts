import MemberServiceProxy from '../service/MemberServiceProxy';
import BoardServiceProxy from '../service/BoardServiceProxy';
import ProjectServiceProxy from '../service/ProjectServiceProxy';
import TeamServiceProxy from '../service/TeamServiceProxy';
import WeClassServiceProxy from '../service/WeClassServiceProxy';

const context = {
    memberServiceProxy: new MemberServiceProxy('http://localhost:8080'),
    boardServiceProxy: new BoardServiceProxy('http://localhost:8080'),
    projectServiceProxy: new ProjectServiceProxy('http://localhost:8080'),
    teamServiceProxy: new TeamServiceProxy('http://localhost:8080'),
    weClassServiceProxy: new WeClassServiceProxy('http://localhost:8080')
}

export default context;

