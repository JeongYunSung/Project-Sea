import * as express from 'express';
import fetch from 'node-fetch';
import { request } from 'graphql-request';

const config = require('./config/my-config.json');

const router = express.Router();

router.get('/authenticate', (req, res) => {
    if(!req.query.token) {
        res.status(400).json({error: '잘못된 토큰값입니다.'});
        return;
    }
    const endpoint = `http://${config.host}:${config.port}/graphql`;
    const query = `
        mutation {
            authenticate(token: "${req.query.token}")
        }
    `;
    request(endpoint, query)
        .then(data => res.send(JSON.stringify(data)))
        .catch(error => res.send(error));
});

router.get('/images', async (req, res) => {
    if(req.query.path) {
        res.status(400).json({error: '경로를 입력해주세요.'});
        return;
    }
    const response = await fetch(`${config.board_uri}/images/${req.query.path}`);
    return await response.blob();
})

export default router;