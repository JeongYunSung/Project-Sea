const resolves = {
    Query: {
        member: () => '정윤성',
        isUsername: function isUsername(_, {username}, context) {
            return context.memberServiceProxy.isUsername(username);
        },
        isNickname: function isNickname(_, {nickname}, context) {
            return context.memberServiceProxy.isNickname(nickname);
        }
    },
    Mutation: {
        signUp: function signUp(_, {username, nickname, password}, context) {
            return context.memberServiceProxy.signUp(username, nickname, password);
        },
        signIn: function signIn(_, {username, password, scopes}, context) {
            return context.memberServiceProxy.signIn(username, password, scopes, context.req.ip, context.tokenManager);
        }
    }
}

export default resolves;