const express = require('express')
const app = express()
const port = 8089
const router = require('./server/route');
// 创建一个服务
let server = require('http').Server(app)
// 创建socket应用
let io = require('socket.io')(server)

router.bindRouters(app, express, io);

server.listen(port, (err) => {
    if (!err) {
        console.log(`Create server on http://localhost:${port}/`);
    } else {
        console.log(err);
    }
});