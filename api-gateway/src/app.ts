import { ApolloServer } from 'apollo-server-express';
import * as express from 'express';
import typeDefs from './graphql/typeDefs';
import resolvers from './graphql/resolvers';
import context from "./graphql/context";
import TokenManager from "./service/TokenManager";
import router from './router';

const config = require('./config/my-config.json');

const tokenManager: TokenManager = new TokenManager();

const app = express();
app.use(router);

const server = new ApolloServer({
    typeDefs,
    resolvers,
    context: ({req}) => {
        if(tokenManager.hasToken(req.ip)) {
            Object.assign(context, {token: tokenManager.getToken(req.ip)});
        }else {
            Object.assign(context, {token: tokenManager.getNullToken()});
        }
        return Object.assign({req}, Object.assign(context, {tokenManager}));
    }
});

server.applyMiddleware({app});

app.listen(config.port, config.host, () => console.log(`Started Server`));