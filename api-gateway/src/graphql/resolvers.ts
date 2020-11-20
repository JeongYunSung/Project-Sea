const resolves = {
    Query: {
        signOut: (_, args, context) => {
            console.log(`${new Date().toISOString()} : signOut processing`);
            return context.tokenManager.removeToken(context.req.ip);
        },
        myProfile: (_, args, context) => {
            console.log(`${new Date().toISOString()} : myProfile processing`);
            return context.memberServiceProxy.myProfile(context.token.accessToken);
        },
        isUsername: (_, {username}, context) => {
            console.log(`${new Date().toISOString()} : isUsername processing`);
            return context.memberServiceProxy.isUsername(username);
        },
        isNickname: (_, {nickname}, context) => {
            console.log(`${new Date().toISOString()} : isNickname processing`);
            return context.memberServiceProxy.isNickname(nickname);
        },
        findBoard: async (_, {id, size}, context) => {
            console.log(`${new Date().toISOString()} : findBoard processing`);
            const board = await context.boardServiceProxy.findBoard(context.token.accessToken, id);
            if(board instanceof Error) return board;
            const comments = await context.boardServiceProxy.findComments(context.token.accessToken, id, 0, size);
            if(comments instanceof Error) return comments;
            return Object.assign(board, {comments});
        },
        findMyBoards: (_, {page, size}, context) => {
            console.log(`${new Date().toISOString()} : findMyBoards processing`);
            return context.boardServiceProxy.findMyBoards(context.token.accessToken, page, size);
        },
        searchBoard: (_, {writer, subject, category, page, size}, context) => {
            console.log(`${new Date().toISOString()} : searchBoard processing`);
            return context.boardServiceProxy.searchBoard(writer, subject, category, page, size);
        },
        bestBoard: (_, {minDate, maxDate, size, category}, context) => {
            console.log(`${new Date().toISOString()} : bestBoard processing`);
            return context.boardServiceProxy.bestBoard(minDate, maxDate, size, category);
        },
        findComments: (_, {id, page, size}, context) => {
            console.log(`${new Date().toISOString()} : findComments processing`);
            return context.boardServiceProxy.findComments(context.token.accessToken, id, page, size);
        },
        findMyNotifications: (_, {page, size}, context) => {
            console.log(`${new Date().toISOString()} : findMyNotifications processing`);
            return context.notificationServiceProxy.findMyNotifications(context.token.accessToken, page, size);
        },
        findNotification: (_, {id}, context) => {
            console.log(`${new Date().toISOString()} : findNotification processing`);
            return context.notificationServiceProxy.findById(context.token.accessToken, id);
        },
        findTeamMembers: (_, {id}, context) => {
            console.log(`${new Date().toISOString()} : findTeamMembers processing`);
            return context.teamServiceProxy.findTeamMembers(context.token.accessToken, id);
        },
        findWeClass: async (_, {id, size}, context) => {
            console.log(`${new Date().toISOString()} : findWeClass processing`);
            const weClass = await context.weClassServiceProxy.findNotice(context.token.accessToken, id);
            if(weClass instanceof Error) return weClass;
            const reports = await context.weClassServiceProxy.findReports(context.token.accessToken, id, 0, size);
            if(reports instanceof Error) return reports;
            return Object.assign(weClass, {reports});
        },
        findReports: (_, {id, page, size}, context) => {
            console.log(`${new Date().toISOString()} : findReports processing`);
            return context.weClassServiceProxy.findReports(context.token.accessToken, id, page, size);
        },
        findReport: (_, {id}, context) => {
            console.log(`${new Date().toISOString()} : findReport processing`);
            return context.weClassServiceProxy.findReport(context.token.accessToken, id);
        },
        searchProject: (_, {subject, state, category, username, page, size}, context) => {
            console.log(`${new Date().toISOString()} : searchProject processing`);
            return context.projectServiceProxy.searchProject(subject, state, category, username, page, size);
        },
        bestProject: (_, {minDate, maxDate, size, category}, context) => {
            console.log(`${new Date().toISOString()} : bestProject processing`);
            return context.projectServiceProxy.bestProject(minDate, maxDate, size, category);
        },
        findMyProject: (_, {page, size}, context) => {
            console.log(`${new Date().toISOString()} : findMyProject processing`);
            return context.projectServiceProxy.findMyProjects(context.token.accessToken, page, size);
        },
        findProject: async (_, {id, size}, context) => {
            console.log(`${new Date().toISOString()} : findProject processing`);
            const project = await context.projectServiceProxy.findProject(context.token.accessToken, id);
            if(project instanceof Error) return project;
            const board = await context.boardServiceProxy.findBoard(context.token.accessToken, project.boardId);
            if(board instanceof Error) return board;
            const comments = await context.boardServiceProxy.findComments(context.token.accessToken, project.boardId, 0, size);
            if(comments instanceof Error) return comments;
            const team = await context.teamServiceProxy.findTeamMembers(context.token.accessToken, project.teamId);
            if(team instanceof Error) return team;
            return Object.assign(project, {board:Object.assign(board, {comments})}, {team});
        }
    },
    Mutation: {
        signUp: (_, {username, nickname, password}, context) => {
            console.log(`${new Date().toISOString()} : signUp processing`);
            return context.memberServiceProxy.signUp(username, nickname, password);
        },
        signIn: (_, {username, password, scopes}, context) => {
            console.log(`${new Date().toISOString()} : signIn processing`);
            return context.memberServiceProxy.signIn(username, password, scopes, context.req.ip, context.tokenManager);
        },
        authenticate: (_, {token}, context) => {
            console.log(`${new Date().toISOString()} : authenticate processing`);
            return context.memberServiceProxy.authenticate(token);
        },
        reviseProfile: (_, {nickname}, context) => {
            console.log(`${new Date().toISOString()} : reviseProfile processing`);
            return context.memberServiceProxy.reviseProfile(context.token.accessToken, nickname);
        },
        createBoard: (_, {subject, content, category, files}, context) => {
            console.log(`${new Date().toISOString()} : createBoard processing`);
            return context.boardServiceProxy.createBoard(context.token.accessToken, subject, content, category, files);
        },
        createComment: (_, {id, commentId, content}, context) => {
            console.log(`${new Date().toISOString()} : createComment processing`);
            return context.boardServiceProxy.createComment(context.token.accessToken, id, commentId, content);
        },
        reviseBoard: (_, {id, subject, content, files}, context) => {
            console.log(`${new Date().toISOString()} : reviseBoard processing`);
            return context.boardServiceProxy.putBoard(context.token.accessToken, id, subject, content, files);
        },
        recommendBoard: (_, {id}, context) => {
            console.log(`${new Date().toISOString()} : recommendBoard processing`);
            return context.boardServiceProxy.recommendBoard(context.token.accessToken, id);
        },
        reviseComment: (_, {id, content}, context) => {
            console.log(`${new Date().toISOString()} : reviseComment processing`);
            return context.boardServiceProxy.putComment(context.token.accessToken, id, content);
        },
        deleteBoard: (_, {id}, context) => {
            console.log(`${new Date().toISOString()} : deleteBoard processing`);
            return context.boardServiceProxy.deleteBoard(context.token.accessToken, id);
        },
        deleteComment: (_, {id}, context) => {
            console.log(`${new Date().toISOString()} : deleteComment processing`);
            return context.boardServiceProxy.deleteComment(context.token.accessToken, id);
        },
        acceptTeam: (_, {authorizeToken}, context) => {
            console.log(`${new Date().toISOString()} : acceptTeam processing`);
            return context.teamServiceProxy.acceptTeam(context.token.accessToken, authorizeToken);
        },
        rejectTeam: (_, {authorizeToken}, context) => {
            console.log(`${new Date().toISOString()} : rejectTeam processing`);
            return context.teamServiceProxy.rejectTeam(context.token.accessToken, authorizeToken);
        },
        joinTeam: (_, {id}, context) => {
            console.log(`${new Date().toISOString()} : joinTeam processing`);
            return context.teamServiceProxy.joinTeam(context.token.accessToken, id);
        },
        quitTeam: (_, {id}, context) => {
            console.log(`${new Date().toISOString()} : quitTeam processing`);
            return context.teamServiceProxy.quitTeam(context.token.accessToken, id);
        },
        createReport: (_, {id, subject, content}, context) => {
            console.log(`${new Date().toISOString()} : createReport processing`);
            return context.weClassServiceProxy.createReport(context.token.accessToken, id, subject, content);
        },
        reviseReport: (_, {id, subject, content}, context) => {
            console.log(`${new Date().toISOString()} : reviseReport processing`);
            return context.weClassServiceProxy.putReport(context.token.accessToken, id, subject, content);
        },
        deleteReport: (_, {id}, context) => {
            console.log(`${new Date().toISOString()} : deleteReport processing`);
            return context.weClassServiceProxy.deleteReport(context.token.accessToken, id);
        },
        createProject: (_, {subject, content, category, min, max, open, lastDate, files}, context) => {
            console.log(`${new Date().toISOString()} : createProject processing`);
            return context.projectServiceProxy.createProject(context.token.accessToken, subject, content, category, min, max, open, lastDate, files);
        },
        reviseProject: (_, {id, subject, content, open, files}, context) => {
            console.log(`${new Date().toISOString()} : reviseProject processing`);
            return context.projectServiceProxy.reviseProject(context.token.accessToken, id, subject, content, open, files);
        },
        cancelProject: (_, {id}, context) => {
            console.log(`${new Date().toISOString()} : cancelProject processing`);
            return context.projectServiceProxy.cancelProject(context.token.accessToken, id);
        }
    }
}

export default resolves;