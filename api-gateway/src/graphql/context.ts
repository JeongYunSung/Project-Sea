import MemberServiceProxy from '../service/MemberServiceProxy';
import BoardServiceProxy from '../service/BoardServiceProxy';
import NotificationServiceProxy from '../service/NotificationServiceProxy';
import ProjectServiceProxy from '../service/ProjectServiceProxy';
import TeamServiceProxy from '../service/TeamServiceProxy';
import WeClassServiceProxy from '../service/WeClassServiceProxy';
import Options from '../service/Options';

const config = require('../config/my-config.json');

const context = {
    memberServiceProxy: new MemberServiceProxy(new Options(config.member_uri)),
    boardServiceProxy: new BoardServiceProxy(new Options(config.board_uri)),
    notificationServiceProxy: new NotificationServiceProxy(new Options(config.notification_uri)),
    projectServiceProxy: new ProjectServiceProxy(new Options(config.project_uri)),
    teamServiceProxy: new TeamServiceProxy(new Options(config.team_uri)),
    weClassServiceProxy: new WeClassServiceProxy(new Options(config.weclass_uri)),
}

export default context;

