import { ApolloServer } from 'apollo-server';
import typeDefs from "./graphql/typeDefs";
import resolvers from './graphql/resolvers';
import context from "./graphql/context";
import TokenManager from "./service/TokenManager";

const config = require('./config/my-config.json');

const tokenManager: TokenManager = new TokenManager();

const server = new ApolloServer({
    typeDefs,
    resolvers,
    context: ({req}) => {
        return Object.assign({req}, context, {tokenManager});
    }
});

server.listen({host: config.host, port: config.port}).then(({url}) => console.log(`Listening at ${url}`));