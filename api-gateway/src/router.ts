import * as express from 'express';
import { request } from 'graphql-request';

const router = express.Router();

router.get('/authenticate', (req, res) => {
    if(!req.query.token) {
        res.status(400).json({error: '잘못된 토큰값입니다.'});
        return;
    }
    const endpoint = 'http://localhost:3000/graphql';
    const query = `
        mutation {
            authenticate(token: "${req.query.token}")
        }
    `;
    request(endpoint, query)
        .then(data => res.send(JSON.stringify(data)))
        .catch(error => res.send(error));
});

export default router;