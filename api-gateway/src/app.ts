import { GraphQLServer } from 'graphql-yoga';
import typeDefs from "./graphql/typeDefs";
import resolvers from './graphql/resolvers';
import context from "./graphql/context";

const server = new GraphQLServer({
    typeDefs,
    resolvers,
    context
});

server.start({port: 3000}, () => console.log("started")).catch(console.error);