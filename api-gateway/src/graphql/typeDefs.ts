const typeDefs = `
    type Member {
        username: String,
        nickname: String
    }
    
    type AccessTokenInfo {
        access_token: String,
        refresh_token: String,
        scope: String
    }

    type Query {
        member(username: String!): Member
        isUsername(username: String!): Boolean
        isNickname(nickname: String!): Boolean
    }

    type Mutation {
        signUp(username: String!, nickname: String!, password: String!): Member
        signIn(username: String!, password: String!, scopes: [String!]!): AccessTokenInfo
    }
`

export default typeDefs;