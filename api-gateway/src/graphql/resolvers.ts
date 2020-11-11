const resolves = {
    Query: {
        signOut: (_, args, context) => {
            return context.tokenManager.removeToken(context.req.ip);
        },
        myProfile: (_, args, context) => {
            return context.memberServiceProxy.myProfile(context.token.accessToken);
        },
        isUsername: (_, {username}, context) => {
            return context.memberServiceProxy.isUsername(username);
        },
        isNickname: (_, {nickname}, context) => {
            return context.memberServiceProxy.isNickname(nickname);
        },
        findBoard: async (_, {id, size}, context) => {
            const board = await context.boardServiceProxy.findBoard(context.token.accessToken, id);
            if(board instanceof Error) return board;
            const comments = await context.boardServiceProxy.findComments(context.token.accessToken, id, 0, size);
            if(comments instanceof Error) return comments;
            return Object.assign(board, {comments});
        },
        findMyBoards: (_, {page, size}, context) => {
            return context.boardServiceProxy.findMyBoards(context.token.accessToken, page, size);
        },
        searchBoard: (_, {writer, subject, category, page, size}, context) => {
            return context.boardServiceProxy.searchBoard(writer, subject, category, page, size);
        },
        bestBoard: (_, {minDate, maxDate, size, category}, context) => {
            return context.boardServiceProxy.bestBoard(minDate, maxDate, size, category);
        },
        findComments: (_, {id, page, size}, context) => {
            return context.boardServiceProxy.findComments(context.token.accessToken, id, page, size);
        },
        findMyNotifications: (_, {page, size}, context) => {
            return context.notificationServiceProxy.findMyNotifications(context.token.accessToken, page, size);
        },
        findNotification: (_, {id}, context) => {
            return context.notificationServiceProxy.findById(context.token.accessToken, id);
        },
        findTeamMembers: (_, {id}, context) => {
            return context.teamServiceProxy.findTeamMembers(context.token.accessToken, id);
        },
        findWeClass: async (_, {id, size}, context) => {
            const weClass = await context.weClassServiceProxy.findNotice(context.token.accessToken, id);
            if(weClass instanceof Error) return weClass;
            const reports = await context.weClassServiceProxy.findReports(context.token.accessToken, id, 0, size);
            if(reports instanceof Error) return reports;
            return Object.assign(weClass, {reports});
        },
        findReports: (_, {id, page, size}, context) => {
            return context.weClassServiceProxy.findReports(context.token.accessToken, id, page, size);
        },
        findReport: (_, {id}, context) => {
            return context.weClassServiceProxy.findReport(context.token.accessToken, id);
        },
        searchProject: (_, {subject, state, category, username, page, size}, context) => {
            return context.projectServiceProxy.searchProject(subject, state, category, username, page, size);
        },
        bestProject: (_, {minDate, maxDate, size, category}, context) => {
            return context.projectServiceProxy.bestProject(minDate, maxDate, size, category);
        },
        findMyProject: (_, {page, size}, context) => {
            return context.projectServiceProxy.findMyProjects(context.token.accessToken, page, size);
        },
        findProject: async (_, {id}, context) => {
            const project = await context.projectServiceProxy.findProject(context.token.accessToken, id);
            if(project instanceof Error) return project;
            const board = await context.boardServiceProxy.findBoard(context.token.accessToken, project.boardId);
            if(board instanceof Error) return board;
            const team = await context.teamServiceProxy.findTeamMembers(context.token.accessToken, project.teamId);
            if(team instanceof Error) return team;
            return Object.assign(project, {board}, {team});
        }
    },
    Mutation: {
        signUp: (_, {username, nickname, password}, context) => {
            return context.memberServiceProxy.signUp(username, nickname, password);
        },
        signIn: (_, {username, password, scopes}, context) => {
            return context.memberServiceProxy.signIn(username, password, scopes, context.req.ip, context.tokenManager);
        },
        authenticate: (_, {token}, context) => {
            return context.memberServiceProxy.authenticate(token);
        },
        reviseProfile: (_, {nickname}, context) => {
            return context.memberServiceProxy.reviseProfile(context.token.accessToken, nickname);
        },
        createBoard: (_, {subject, content, category, files}, context) => {
            return context.boardServiceProxy.createBoard(context.token.accessToken, subject, content, category, files);
        },
        createComment: (_, {id, commentId, content}, context) => {
            return context.boardServiceProxy.createComment(context.token.accessToken, id, commentId, content);
        },
        reviseBoard: (_, {id, subject, content, files}, context) => {
            return context.boardServiceProxy.putBoard(context.token.accessToken, id, subject, content, files);
        },
        recommendBoard: (_, {id}, context) => {
            return context.boardServiceProxy.recommendBoard(context.token.accessToken, id);
        },
        reviseComment: (_, {id, content}, context) => {
            return context.boardServiceProxy.putComment(context.token.accessToken, id, content);
        },
        deleteBoard: (_, {id}, context) => {
            return context.boardServiceProxy.deleteBoard(context.token.accessToken, id);
        },
        deleteComment: (_, {id}, context) => {
            return context.boardServiceProxy.deleteComment(context.token.accessToken, id);
        },
        acceptTeam: (_, {authorizeToken}, context) => {
            return context.teamServiceProxy.acceptTeam(context.token.accessToken, authorizeToken);
        },
        rejectTeam: (_, {authorizeToken}, context) => {
            return context.teamServiceProxy.rejectTeam(context.token.accessToken, authorizeToken);
        },
        joinTeam: (_, {id}, context) => {
            return context.teamServiceProxy.joinTeam(context.token.accessToken, id);
        },
        quitTeam: (_, {id}, context) => {
            return context.teamServiceProxy.quitTeam(context.token.accessToken, id);
        },
        createReport: (_, {id, subject, content}, context) => {
            return context.weClassServiceProxy.createReport(context.token.accessToken, id, subject, content);
        },
        reviseReport: (_, {id, subject, content}, context) => {
            return context.weClassServiceProxy.putReport(context.token.accessToken, id, subject, content);
        },
        deleteReport: (_, {id}, context) => {
            return context.weClassServiceProxy.deleteReport(context.token.accessToken, id);
        },
        createProject: (_, {subject, content, category, min, max, open, lastDate, files}, context) => {
            return context.projectServiceProxy.createProject(context.token.accessToken, subject, content, category, min, max, open, lastDate, files);
        },
        reviseProject: (_, {id, subject, content, open, files}, context) => {
            return context.projectServiceProxy.reviseProject(context.token.accessToken, id, subject, content, open, files);
        },
        cancelProject: (_, {id}, context) => {
            return context.projectServiceProxy.cancelProject(context.token.accessToken, id);
        }
    }
}

export default resolves;