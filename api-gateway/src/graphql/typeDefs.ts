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
        createdTime: String,
        comments: CommentPage
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
    
    type Team {
        minSize: Int,
        maxSize: Int,
        teamState: String,
        members: [TeamMember]
    }
    
    type TeamMember {
        username: String,
        teamPermission: String,
        teamMemberState: String
    }
    
    type Notification {
        id: Float,
        subject: String,
        content: String,
        read: Boolean,
        createdTime: String,
    }
    
    type NotificationPage {
        notifications: [Notification],
        page: Page
    }
    
    type WeClass {
        notice: String,
        reports: ReportPage
    }
    
    type Report {
        writer: String,
        subject: String,
        content: String,
        createdTime: String
    }
    
    type ReportPage {
        reports: [Report],
        page: Page
    }
    
    type Project {
        id: Float,
        subject: String,
        category: String,
        projectState: String,
        recommendCount: Float,
        createdTime: String
    }
    
    type ProjectPage {
        projects: [Project],
        page: Page
    }
    
    type ProjectDetail {
        id: Float,
        board: Board,
        team: Team,
        weClassId: Float,
        state: String,
        createdTime: String
    }

    type Query {
        signOut: Boolean
        myProfile: Member
        isUsername(username: String!): Boolean
        isNickname(nickname: String!): Boolean
        findBoard(id: Float!, size: Int): Board
        findMyBoards(page: Int, size: Int): BoardPage
        searchBoard(writer: String, subject: String, category: String, page: Float, size: Float): BoardPage
        bestBoard(minDate: String!, maxDate: String!, size: Int!, category: String): [Board]
        findComments(id: Float!, page: Int, size: Int): CommentPage
        findMyNotifications(page: Int, size: Int): NotificationPage
        findNotification(id: Float!): Notification
        findTeamMembers(id: Float!): Team
        findWeClass(id: Float!, size: Int): WeClass
        findReports(id: Float!, page: Int, size: Int): ReportPage
        findReport(id: Float!): Report
        searchProject(subject: String, state: String, category: String, username: String, page: Int, size: Int): ProjectPage
        bestProject(minDate: String!, maxDate: String!, size: Int!, category: String): [Project]
        findMyProject(page: Int, size: Int): ProjectPage
        findProject(id: Float!): ProjectDetail
    }

    type Mutation {
        signUp(username: String!, nickname: String!, password: String!): Member
        signIn(username: String!, password: String!, scopes: [String!]!): AccessTokenInfo
        authenticate(token: String!): Boolean
        reviseProfile(nickname: String!): Member
        createBoard(subject: String!, content: String!, category: String!, files: [Upload]): Float
        createComment(id: Float!, commentId: Float, content: String!): Float
        reviseBoard(id: Float!, subject: String!, content: String!, files: [Upload]): Float
        recommendBoard(id: Float!): Float
        reviseComment(id: Float!, content: String!): Float
        deleteBoard(id: Float!): Boolean
        deleteComment(id: Float!): Boolean
        acceptTeam(authorizeToken: String!): Boolean
        rejectTeam(authorizeToken: String!): Boolean
        joinTeam(id: Float!): Float
        quitTeam(id: Float!): Boolean
        createReport(id: Float!, subject: String!, content: String!): Float
        reviseReport(id: Float!, subject: String!, content: String!): Boolean
        deleteReport(id: Float!): Boolean
        createProject(subject: String!, content: String!, category: String!, min: Int!, max: Int!, open: Boolean!, lastDate: String!, files: [Upload]): Float
        reviseProject(id: Float!, subject: String!, content: String!, open: Boolean!, files: [Upload]): Float
        cancelProject(id: Float!): Float
        
    }
`

export default typeDefs;