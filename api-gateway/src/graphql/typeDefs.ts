const typeDefs = `
    type Member {
        username: String,
        nickname: String,
        permission: String,
        createdTime: String
    }
    
    type Board {
        id: Float,
        writer: String,
        subject: String,
        content: String,
        category: String,
        readCount: Float,
        recommendCount: Float,
        createdTime: String
    }
    
    type BoardPage {
        boards: [Board],
        page: Page
    }
    
    type Comment {
        id: Float,
        writer: String,
        content: String,
        createdTime: String,
        commentState: String
    }
    
    type CommentPage {
        comments: [Comment],
        page: Page
    }
    
    type AccessTokenInfo {
        access_token: String,
        refresh_token: String,
        scope: String
    }
    
    type Page {
        size: Int,
        totalElements: Int,
        totalPages: Int,
        number: Int
    }

    type Query {
        myProfile: Member
        isUsername(username: String!): Boolean
        isNickname(nickname: String!): Boolean
        findBoard(id: Float!): Board
        searchBoard(writer: String, subject: String, category: String, recommendCount: Float, page: Float, size: Float): BoardPage
        findComments(id: Float!, page: Float, size: Float): CommentPage
    }

    type Mutation {
        signUp(username: String!, nickname: String!, password: String!): Member
        signIn(username: String!, password: String!, scopes: [String!]!): AccessTokenInfo
        authenticate(token: String!): Boolean
        reviseProfile(nickname: String!): Member
        createBoard(subject: String!, content: String!, category: String!, files: [Upload]): Float
        createComment(id: Float!, commentId: Float, content: String!): Float
        putBoard(id: Float!, subject: String!, content: String!): Float
        recommendBoard(id: Float!): Float
        putComment(id: Float!, content: String!): Float
        deleteBoard(id: Float!): Boolean
        deleteComment(id: Float!): Boolean
    }
`

export default typeDefs;