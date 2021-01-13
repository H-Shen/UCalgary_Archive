const session = require("express-session");
const bodyParser = require('body-parser')
const path = require("path");
const service = require("./service")

let IO = null;
exports.bindRouters = (app, express, io) => {
    app.use(session({
        secret: "We should get at least A because we can",
        name: "session_id",
        resave: false,
        saveUninitialized: true,
        //cookie: {maxAge: 5000},
        rolling: true
    }))
    // app.use(bodyParser.json());
    // app.use(bodyParser.urlencoded({ limit:'21000000kb', extended: true }));
    app.use(bodyParser.json({limit:'100mb'}));
    app.use(bodyParser.urlencoded({ limit:'100mb', extended: true }));  
    app.use(express.static(path.resolve(__dirname + "/../css")));
    app.use(express.static(path.resolve(__dirname + "/../js")));
    app.use(express.static(path.resolve(__dirname + "/../image")));
    app.use(express.static(path.resolve(__dirname + "/../fonts")));
    console.log("app配置完成")
    IO = io;
    let messages = [];
    // 保存链接的地方
    let sockets = [];
    io.on('connect', (socket) => {
        socket.on('project-update', data => {
            const { teamID, username } = data;
            // 广播告知所有用户
            for (const item of sockets) {
                if(item.teamID == teamID && item.page == "group-project-edit" && item.name != username){
                    item.emit('project-update', data)
                }
            }
        })
        // 处理用户发送信息
        socket.on('messageUp', data => {
            const date = new Date().toLocaleString();
            const { msg, user, teamID } = data;
            let message = {
                time: date,
                name: user,
                msg,
                teamID
            }
            messages.push(message);
            // 广播告知所有用户
            for (const item of sockets) {
                if (item.teamID == teamID) {
                    item.emit('messageDown', message)
                }
            }
        })
        // 监听用户登录的功能
        socket.on('login', data => {
            const { username, page, teamID } = data;
            // 广播告知新人加入
            socket.name = username;
            socket.page = page;
            socket.teamID = teamID;
            let exit = sockets.find((item) => {
                return item.name == username;
            })
            if (exit) {
                exit = socket;
            } else {
                sockets.push(socket);
            }
            if (page === "group-project-edit") {
                const watcher = [];
                const watcherName = [];
                for (const socket of sockets) {
                    if(socket.teamID == teamID && socket.page == "group-project-edit"){
                        watcher.push(socket);
                        watcherName.push(socket.name);
                    }
                }
                for (const item of watcher) {
                    item.emit('someoneLogin', username);
                    item.emit('updateWatcher', watcherName);
                }
            }
        })
        // 处理用户断开链接
        socket.on('disconnect', () => {
            sockets = sockets.filter((item) => {
                return item.name != socket.name;
            })
            if (socket.page === "group-project-edit") {
                const { teamID } = socket;
                const watcher = [];
                const watcherName = [];
                for (const socket of sockets) {
                    if(socket.teamID == teamID && socket.page == "group-project-edit"){
                        watcher.push(socket);
                        watcherName.push(socket.name);
                    }
                }
                for (const item of watcher) {
                    item.emit('someoneLeave', {
                        name: socket.name
                    })
                    item.emit('updateWatcher', watcherName);
                }
            }
            // 广播告知有人退群
        })
    })
    setRouterSequence(app);
}


let setRouterSequence = (app) => {
    // index: main page
    app.get("/", (req, res) => {
        if (req.session.username !== undefined) {
            res.contentType("text/html").sendFile(path.resolve(__dirname + "/../html/user_profile.html"), (err) => {
                console.error(err)
            });
        } else {
            res.contentType("text/html").sendFile(path.resolve(__dirname + "/../html/index.html"), (err) => {
                console.error(err);
            });
        }
    });

    app.get("/index.html", (req, res) => {
        if (req.session.username !== undefined) {
            res.contentType("text/html").sendFile(path.resolve(__dirname + "/../html/user_profile.html"), (err) => {
                console.error(err)
            });
        } else {
            res.contentType("text/html").sendFile(path.resolve(__dirname + "/../html/index.html"), (err) => {
                console.error(err);
            });
        }
    });

    app.get("/user_profile.html", (req, res) => {
        if (req.session.username !== undefined) {
            res.contentType("text/html").sendFile(path.resolve(__dirname + "/../html/user_profile.html"), (err) => {
                console.error(err)
            });
        } else {
            res.contentType("text/html").sendFile(path.resolve(__dirname + "/../html/index.html"), (err) => {
                console.error(err);
            });
        }
    });

    app.post('/:req_name', (req, res) => {
        let JSOName = req.params["req_name"]
        let ret;
        switch (JSOName) {
            case "login":
                service.loginJSON(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;
            case "getProblem":
                service.getProblem(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;
            case "create_account":
                service.create_accountJSON(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;
            case "recover":
                service.changeUserInfo(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;
            case "user_profile":
                service.user_profileJSON(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;
            case "add_member":
                service.addMember(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;
            case "add_team":
                service.addTeam(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;
            case "createGroup":
                service.createGroup(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        if (r.code == 200) {
                            // 世界广播小组创建
                            // IOteamCreate()
                            IO.emit("teamCreate");
                        }
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;
            case "queryGroups":
                service.queryGroups(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;
            case "queryMembersById":
                service.queryMembersById(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;
            case "queryProjectById":
                service.queryProjectById(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;
            case "leaveOrJoinGroup":
                service.leaveOrJoinGroup(req, res).then((r) => {
                    ret = r
                    if (ret !== undefined) {
                        if (r.code == 200) {
                            // 世界广播加入或离开小组
                            IO.emit("leaveOrJoinGroup");
                        }
                        res.json(ret);
                    } else {
                        res.sendStatus(404);
                    }
                }).catch((e) => {
                    console.error(e);
                    res.sendStatus(500);
                });
                break;

            case "addProjectS":
                    service.addProjectS(req, res).then((r) => {
                        ret = r
                        if (ret !== undefined) {
                            if (r.code == 200&&r.team) {
                                //创建成功，需要更新团队页面
                                IO.emit("addProjectS_success");
                            }
                            res.json(ret);
                        } else {
                            res.sendStatus(404);
                        }
                    }).catch((e) => {
                        console.error(e);
                        res.sendStatus(500);
                    });
                    break;
           case "addTeamProject":
                        service.addTeamProject(req, res).then((r) => {
                            ret = r
                            if (ret !== undefined) {
                                res.json(ret);
                            } else {
                                res.sendStatus(404);
                            }
                        }).catch((e) => {
                            console.error(e);
                            res.sendStatus(500);
                        });
                        break;
    
            case "changeProject":
                        service.changeProject(req, res).then((r) => {
                            ret = r
                            if (ret !== undefined) {
                                if (r.code == 200&&r.team) {
                                    //创建成功，需要更新团队页面
                                    IO.emit("changeProject_success");
                                    console.log("changeProject_success")
                                }
                                res.json(ret);
                            } else {
                                res.sendStatus(404);
                            }
                        }).catch((e) => {
                            console.error(e);
                            res.sendStatus(500);
                        });
                        break;
            case "delete_session":
                res.json(service.delete_sessionJSON(req, res));
                break;
            default:
                res.sendStatus(404);
        }
    }
    )

    app.get("/:h_n.html", (req, res, next) => {
        let pageName = req.params["h_n"];
        switch (pageName) {
            // No session pages.
            case "index":
            case "register":
                res.contentType("text/html").sendFile(path.resolve(__dirname + "/../html/" + req.params["h_n"] + ".html"), (err) => {
                    console.error(err)
                });
                break;
            case "":
                next();
                break;
            case undefined:
                next();
                break;
            default:
                if (req.session.username !== undefined) {
                    res.contentType("text/html").sendFile(path.resolve(__dirname + "/../html/" + pageName + ".html"), (err) => {
                        console.error(err)
                    });
                } else {
                    if (pageName == "account_recovery") {
                        res.contentType("text/html").sendFile(path.resolve(__dirname + "/../html/" + pageName + ".html"), (err) => {
                            console.error(err)
                        });
                    } else {
                        res.contentType("text/html").sendFile(path.resolve(__dirname + "/../html/index.html"), (err) => {
                            console.error(err)
                        });
                    }
                }
        }
    });
}






