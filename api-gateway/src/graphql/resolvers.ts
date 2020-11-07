const resolves = {
    Query: {
        myProfile: function myProfile(_, args, context) {
            return context.memberServiceProxy.myProfile(context.token.accessToken);
        },
        isUsername: function isUsername(_, {username}, context) {
            return context.memberServiceProxy.isUsername(username);
        },
        isNickname: function isNickname(_, {nickname}, context) {
            return context.memberServiceProxy.isNickname(nickname);
        },
        findBoard: function findBoard(_, {id}, context) {
            return context.boardServiceProxy.findBoard(context.token.accessToken, id);
        },
        searchBoard: function findBoard(_, {writer, subject, category, recommendCount, page, size}, context) {
            return context.boardServiceProxy.searchBoard(context.token.accessToken, writer, subject, category, recommendCount, page, size);
        },
        findComments: function findBoard(_, {id, page, size}, context) {
            return context.boardServiceProxy.findComments(context.token.accessToken, id, page, size);
        }
    },
    Mutation: {
        signUp: function signUp(_, {username, nickname, password}, context) {
            return context.memberServiceProxy.signUp(username, nickname, password);
        },
        signIn: function signIn(_, {username, password, scopes}, context) {
            return context.memberServiceProxy.signIn(username, password, scopes, context.req.ip, context.tokenManager);
        },
        authenticate: function authenticate(_, {token}, context) {
            return context.memberServiceProxy.authenticate(token);
        },
        reviseProfile: function reviseProfile(_, {nickname}, context) {
            return context.memberServiceProxy.reviseProfile(context.token.accessToken, nickname);
        },
        createBoard: function createBoard(_, {subject, content, category, files}, context) {
            return context.boardServiceProxy.createBoard(context.token.accessToken, subject, content, category, files);
        },
        createComment: function createComment(_, {id, commentId, content}, context) {
            return context.boardServiceProxy.createComment(context.token.accessToken, id, commentId, content);
        },
        putBoard: function putBoard(_, {id, subject, content}, context) {
            return context.boardServiceProxy.putBoard(context.token.accessToken, id, subject, content);
        },
        recommendBoard: function putBoard(_, {id}, context) {
            return context.boardServiceProxy.recommendBoard(context.token.accessToken, id);
        },
        putComment: function putComment(_, {id, content}, context) {
            return context.boardServiceProxy.putComment(context.token.accessToken, id, content);
        },
        deleteBoard: function deleteBoard(_, {id}, context) {
            return context.boardServiceProxy.deleteBoard(context.token.accessToken, id);
        },
        deleteComment: function deleteComment(_, {id}, context) {
            return context.boardServiceProxy.deleteComment(context.token.accessToken, id);
        }
    }
}

export default resolves;