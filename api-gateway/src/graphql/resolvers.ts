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
        }
    }
}

export default resolves;