const typeDefs = `
    type Member {
        username: String,
        nickname: String,
        permission: String,
        createdTime: String
    }
    
    type AccessTokenInfo {
        access_token: String,
        refresh_token: String,
        scope: String
    }

    type Query {
        myProfile: Member
        isUsername(username: String!): Boolean
        isNickname(nickname: String!): Boolean
    }

    type Mutation {
        signUp(username: String!, nickname: String!, password: String!): Member
        signIn(username: String!, password: String!, scopes: [String!]!): AccessTokenInfo
        authenticate(token: String!): Boolean
        reviseProfile(nickname: String!): Member
    }
`

export default typeDefs;