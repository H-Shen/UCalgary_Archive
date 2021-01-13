const model = require("./model");
const fs = require("fs");
const path = require("path");
// Return -2 if the username or the password from request is empty!!
// Return -1 if such username does not exist
// Return 0 if such password does not match the username
// Return 1 if the username exists and matches the password
// Return 2 if other errors happen

exports.loginJSON = async (req, res) => {
    // console.log("loginJSON--req:",req)
    console.log("loginJSON--req.body:", req.body)
    if (req.session.username) {
        return { "ret": "1" }; 
    } else {
        if (req.body) {
            console.log(req.body);
        } else {
            console.log("body is none")
        }
        if (!req.body.username || !req.body.password) {
            return { "ret": "-2" }
        }
        let ret;
        await model.getPassByUser(req.body.username).then((r) => {
            console.log(JSON.stringify(r));
            if (r !== undefined) {
                console.log("password : " + r["password"]);
                if (req.body.password === r["password"]) {
                    req.session.username = req.body.username;
                    ret = "1";
                } else {
                    ret = "0";
                }
            } else {
                ret = "-1";
            }
        }).catch((e) => {
            console.error(e);
            //ret = "2";
            throw new Error("User Login checking Error in the Database!!!");
        })
        return {
            "ret": ret
        };
    }
}

exports.changeUserInfo = async (req, res) => {
    console.log("changeUserInfo--req.body:", req.body)
    if (req.body) {
        console.log(req.body);
    } else {
        console.log("body is none")
    }

    let resObj = {
        code: 200,
    }
    if (!req.body.username || !req.body.newPassword) {
        return {
            code: 0,
            msg: "参数不正确"
        }
    }
    let ret;
    await model.changeUserInfo(req.body.username, req.body.newPassword).then((r) => {
        console.log(JSON.stringify(r));
        if (r !== undefined) {
            resObj.data = r
        } else {
            resObj.code = 0;
            resObj.msg = "修改失败"
        }
    }).catch((e) => {
        console.error(e);
        resObj.code = 0;
        resObj.msg = JSON.stringify(e)
        //ret = "2";
        throw new Error("User Login checking Error in the Database!!!");
    })
    return resObj;
}

exports.create_accountJSON = async (req, res) => {
    let body = req.body;
    if (body) {
        console.log(body);
    } else {
        console.log("body is none");
    }

    for (let key in req.body) {
        if (key === undefined) {
            return { "ret": "-2" }
        }
    }

    let ret;
    await model.addUserInfo(body.username, body.password, body.email, body.question, body.answer)
        .then((r) => {
            console.log("affectRows : " + r)
            if (r === 1) {
                // req.session.username = body.username;
                ret = 0;
            } else {
                ret = -1;
            }
        }).catch((e) => {
            console.log(e)
            throw new Error("CreateAccount Error in the databases!!!");
        })
    return {
        "ret": ret
    };
}
exports.getProblem = async (req, res) => {
    // console.log("getProblem--req:",req)
    console.log("getProblem--req.body:", req.body)
        if (req.body) {
            console.log(req.body);
        } else {
            console.log("body is none")
        }
        let resObj = {
            code: 0,
            data: {},
            msg: ""
        };
        if (!req.body.username) {
            resObj.msg="用户不存在"
            return resObj
        }
       
        await model.getUserInfo(req.body.username).then((r) => {
            console.log(JSON.stringify(r));
            if (r !== undefined) {
                resObj.code = 200
                resObj.data = r;
            } else {
                resObj.code = 0;
                resObj.msg = "用户不存在";
            }
        }).catch((e) => {
            console.error(e);
            resObj.code = 0;
            resObj.msg = JSON.stringify(e);
            throw new Error("User Login checking Error in the Database!!!");
        })
        return resObj;
}


exports.user_profileJSON = async (req, res) => {
    // if (!req.session.username) {
    //     return {"ret": "1"};
    // }
    console.log("user_profileJSON--req.body:", req.body)
    let session = req.session;
    if (session) {
        console.log(session);
    } else {
        console.log("Session is none");
    }
    // if (!req.body.username || !req.body.password) {
    //     return {"ret": "-2"}
    // }
    let ret;
    let resObj = {
        code:200,
        msg:""
    }
    await model.getUserInfo(
        session.username
    ).then((r) => {
        if (r["username"] !== undefined && r["email"] !== undefined) {
            ret = {
                "returnCode": "1",
                code:200,
                data:r,
                "userInfo": {
                    "username": r.username,
                    "user_email": r.email
                }
            };

        } else {
            throw new Error("Data From DataBase is not correct!!!");
        }
    }).catch((e) => {
        throw e;
    })
    resObj.ret = ret;
    return resObj
}


exports.delete_sessionJSON = (req, res) => {
    try {
        console.log(req.session["username"]);
        delete req.session["username"];
        return { "ret": "1" }
    } catch (e) {
        res.statusCode(500);
        return { "ret": "-1" }
    }
}

// -2 means the user haven't login yet
exports.group_profile_get_infoJSON = async (req, res) => {
    console.log("group_profile_get_infoJSON--req:", req)
    console.log("group_profile_get_infoJSON--req.session.username:", req.session.username)
    if (!(req.session.username)) {
        return { "ret": "-2" };
    }

    let ret;
    await model.getTeamInfo().then((r) => {
        if (!req.session.username) {
            return { "ret": "-2" };
        }
        //r is a object
        ret = {
            "returnCode": "1",
            "info": r
        };
    }).catch((e) => {
        console.error(e);
        res.sendStatus(500);
    })
    return {
        "ret": ret
    };
}

exports.addTeam = async (req, res) => {
    if (!req.session.username) {
        return { "ret": "-2" };
    }
    let teamID = "";
    for (let i = 0; i < 6; i++) {
        teamID += Math.floor(Math.random() * 10);
    }

    let ret;
    await model.addTeam(
        teamID
    ).then((r) => {
        //r is a affectRows
        if (r = 1) {
            ret = {
                "returnCode": "1",
                "teamID": teamID,
                "username": req.session.username
            };
            //teamID:};
        } else {
            ret = { "returnCode": "-1" }
        }
    }).catch((e) => {
        console.error(e);
        res.sendStatus(500);
    })
    return {
        "ret": ret
    };
}

exports.addMember = async (req, res) => {
    // no login
    if (!req.session.username) {
        return { "ret": "-2" };
    }

    console.log(req.body.username);
    console.log(req.body.teamID);

    let username = req.body.username;

    let isUserExist = false;
    let ret;
    await model.getUserInfo(username).then((r) => {
        console.log(r);
        if (r !== undefined) {
            isUserExist = true;
            return;
        } else {

            // not the user in database
            ret = { "returnCode": "0" };
        }
    });

    if (!isUserExist) {
        return ret;
    }

    let isAlreadyInThisTeams = true;
    await model.getTeamsByUsername(
        req.body.username
    ).then((r) => {
        console.log(r);
        console.log("lalala1");

        if (r.length === 0) {
            console.log("lalala2");
            isAlreadyInThisTeams = false;
            return;
        } else {
            let teamID = req.body.teamID;
            console.log("lalala3");
            if (typeof (teamID) === "number") {
                console.log("lalala4");
                teamID = String(teamID);
            }
            console.log(r);
            let tid
            for (tid of r) {
                console.log("lalalala : " + tid);
                console.log("lalalalaxi : " + r);

                if (tid === teamID) {
                    console.log("lalala5" + tid);

                    // Already in the aiming team
                    ret = { "returnCode": "-3" };
                    return;
                }
                console.log("lalala6" + tid);
            }
            isAlreadyInThisTeams = false;
        }
    });

    if (isAlreadyInThisTeams) {
        console.log("lalala6")
        return ret;
    }

    await model.addTeamMember(
        req.body.username,
        req.body.teamID
    ).then((r) => {
        //r is a affectRows
        //0 means the user is not exist

        if (r = 1) {
            // Can be add into the team
            ret = { "returnCode": "1", "username": req.body.username };
        } else {
            // Unknown error
            ret = { "returnCode": "-1" };
        }
    }).catch((e) => {
        console.error(e);
        res.sendStatus(500);
    })
    return {
        "ret": ret
    };
}



// ---------------------------------------------------------------------
// ---------------------------------------------------------------------
// ---------------------------------------------------------------------
// ---------------------------------------------------------------------
// ---------------------------------------------------------------------
// --------------------------start-new-------------------------------------
// ---------------------------------------------------------------------
// ---------------------------------------------------------------------
// ---------------------------------------------------------------------
// ---------------------------------------------------------------------


/**
 * 创建分组
 * teamID
 * memberID
 */

exports.createGroup = async (req, res) => {
    if (!req.session.username) {
        return { "ret": "-2" };
    }
    let resObj = {
        code: 200,
        msg: "成功"
    }
    let teamID = req.body.teamID;
    let memberID = req.body.memberID;

    if (!teamID || !memberID) {
        resObj.code = 0;
        resObj.msg = "参数不全！"
        return resObj
    }
    await model.createGroup(
        teamID,
        memberID
    ).then((r) => {
        if (r <= 0) {
            resObj.code = 0;
            resObj.msg = "创建失败！"
        }
    }).catch((e) => {
        console.error(e);
        resObj.code = 500;
        resObj.msg = JSON.stringify(e)
        return resObj;
        res.sendStatus(500);
    })
    return resObj
}


/**
 * 
 * 查询分组
 * 
 * 如果无参数 就直接查所有的
 * 
 * 如果有参数teamID就查某一个分组

 */

exports.queryGroups = async (req, res) => {
    console.log("queryGroups-req:",req.session)
    if (!req.session.username) {
        console.log("queryGroups-req.session.username:",req.session.username)
        return { "ret": "-2" };
    }
    let resObj = {
        code: 200,
        msg: "成功"
    }
    let teamID = req.body.teamID;
    await model.queryGroups(
            teamID?teamID:""
        ).then((r) => {
        if(r){
             r.map(item=>{
                let arr = [];
                arr=item.member.split(",").filter(item=>{
                    return item
                })
                item.member=arr.join(",")
            })
            console.log(r);
            resObj.data=r;
        }
    }).catch((e) => {
        console.error(e);
        resObj.code = 500;
        resObj.msg = JSON.stringify(e)
        return resObj;
        res.sendStatus(500);
    })
    return resObj
}


/**
 * 
 * 查询成员
 * 
memberID：传username数组 
示例："memberID":["JamesOC2"]

不传就是查所有

 */

exports.queryMembersById = async (req, res) => {
    console.log("queryMembersById-req:",req.session)
    console.log("queryMembersById-req.body:",req.body)
    if (!req.session.username) {
        console.log("queryMembersById-req.session.username:",req.session.username)
        return { "ret": "-2" };
    }
    let resObj = {
        code: 200,
        msg: "成功"
    }
    let str = "";
    if(req.body.memberID && req.body.memberID instanceof Array){
        let memberID = req.body.memberID
        if(memberID.length>0){
            memberID.map((item,index)=>{
                str+=`"${item}"`;
                if(index!==memberID.length-1){
                    str+=","
                }
            })
        }
    }
    await model.queryMembersById(
            str
        ).then((r) => {
        resObj.data=r;
    }).catch((e) => {
        console.error(e);
        resObj.code = 500;
        resObj.msg = JSON.stringify(e)
        return resObj;
        res.sendStatus(500);
    })
    return resObj
}




/**
 * 
 * 根据  项目id  查询项目
 * 
projectID：传projectID数组 
示例："projectID":["123546"]


不传就是查所有

 */

exports.queryProjectById = async (req, res) => {
    console.log("queryProjectById-req:",req.session)
    console.log("queryProjectById-req.body:",req.body)
    if (!req.session.username) {
        console.log("queryProjectById-req.session.username:",req.session.username)
        return { "ret": "-2" };
    }
    let resObj = {
        code: 200,
        msg: "成功"
    }
    let str = "";
    if(req.body.projectID && req.body.projectID instanceof Array){
        let arr = req.body.projectID
        if(arr.length>0){
            arr.map((item,index)=>{
                str+=`"${item}"`;
                if(index!==arr.length-1){
                    str+=","
                }
            })
        }
    }
    console.log("queryProjectById-str:",str)

    await model.queryProjectById(
            str
        ).then((r) => {
        resObj.data=r;
    }).catch((e) => {
        console.error(e);
        resObj.code = 500;
        resObj.msg = JSON.stringify(e)
        return resObj;
        res.sendStatus(500);
    })
    return resObj
}



/**
 * 离开或者加入分组
 * teamID
 * memberID:"1,2,4,5,7" // 去掉某个人后的分组中的所有成员id    或者加入后的
 */

exports.leaveOrJoinGroup = async (req, res) => {
    if (!req.session.username) {
        return { "ret": "-2" };
    }
    let resObj = {
        code: 200,
        msg: "成功"
    }
    let teamID = req.body.teamID;
    let memberID = req.body.memberID;

    if (!teamID) {
        resObj.code = 0;
        resObj.msg = "参数不全！"
        return resObj
    }
    await model.leaveOrJoinGroup(
        teamID,
        memberID
    ).then((r) => {
        if (r <= 0) {
            resObj.code = 0;
            resObj.msg = "失败！"
        }
    }).catch((e) => {
        console.error(e);
        resObj.code = 500;
        resObj.msg = JSON.stringify(e)
        return resObj;
        res.sendStatus(500);
    })
    return resObj
}


/*
存储图片
*/

let saveImg=async(url,imgName)=>{
    var base64Data = url.replace(/^data:image(\/png|\/jpeg|\/jpg);base64,/, "");
    //  url = new Buffer(url , 'base64').toString();
    // console.log("addProjectS----base64Data:",base64Data)
    let name = imgName?imgName:(new Date()).valueOf()+ "";
    name = `${name}.jpg`;
    let dirPath = path.join(__dirname,"../image/"+name);
    fs.writeFileSync(dirPath, base64Data, 'base64', function (err,data) {
        console.log("writeFile---err:",err)
        console.log("writeFile---data:",data)
    });
    console.log("saveImg--name:",name)
    return name
}

/**
 * 添加项目
 * projectID
 * url 传base64
 * rotate //非必传
 * turn   //非必传
 * opacity//非必传
light//非必传
 * 
 * 
 * 1、
 * teamID //如果传这个 就说明是团队的项目 团队表这个id里面的也需要传 projectID
 * teamProjectID  同时传当前team所有的项目id  示例：teamProjectID:"1,2,5,6,7"
 * 
 * 2、
 * username //如果传这个 就说明是个人的项目 用户表这个id里面的也需要传 projectID
   usernameProjectID  同时传当前team所有的项目id  示例：usernameProjectID:"1,2,5,6,7"


 * 1和2 两者至少要一个   最多也只能有一个
 */

exports.addProjectS = async (req, res) => {
    if (!req.session.username) {
        return { "ret": "-2" };
    }
    let resObj = {
        code: 200,
        msg: "成功",
        
    }
    let projectID = req.body.projectID;
    
    let url = req.body.url;
    let rotate = req.body.rotate;
    let opacity = req.body.opacity;
    let light = req.body.light;
    let turn = req.body.turn;
    let teamID = req.body.teamID;
    let username = req.body.username;
    let teamProjectID = req.body.teamProjectID;
    let usernameProjectID = req.body.usernameProjectID;
    
    if (!teamID && !username) {
        resObj.code = 0;
        resObj.msg = "参数不全1！"
        return resObj
    }
    if (!projectID || !url) {
        resObj.code = 0;
        resObj.msg = "参数不全2！"
        return resObj
    }
    if (teamID && username) {
        resObj.code = 0;
        resObj.msg = "参数错误！"
        return resObj
    }
    if(teamID){
        //说明是团队的更新  socket就只能让团队的更新
        resObj.team = true;
    }
   
    // console.log("url---1:",url)
    let imgUrl = await saveImg(url);
    await model.addProjectS(
        projectID,
        imgUrl,
        rotate,
        turn,
        opacity,
        light,
        teamID,
        username,
        teamProjectID,
        usernameProjectID,
    ).then((r) => {
        if (r <= 0) {
            resObj.code = 0;
            resObj.msg = "失败！"
        }else{
            resObj.projectID = projectID;
            resObj.imgUrl = imgUrl;
        }
    }).catch((e) => {
        console.error(e);
        resObj.code = 500;
        resObj.msg = JSON.stringify(e)
        return resObj;
        res.sendStatus(500);
    })
    console.log("addProjectS---resObj:",resObj);
    return resObj
}


/**
 * 修改项目
 * projectID
 * url    传base64
 * imgName  名称  //用来修改图片覆盖   不需要后缀名 只需要名称
 * username //非必穿 用来判断是团队还是个人页面
 * rotate //非必传
 * turn   //非必传
 * opacity   //非必传
 * light   //非必传
 */

exports.changeProject = async (req, res) => {
    if (!req.session.username) {
        return { "ret": "-2" };
    }
    let resObj = {
        code: 200,
        msg: "成功"
    }
    let projectID = req.body.projectID;
    let url = req.body.url;
    let imgName = req.body.imgName;
    let rotate = req.body.rotate;
    let opacity = req.body.opacity;
    let light = req.body.light;
    let turn = req.body.turn;
    let username = req.body.username;

    if (!projectID ||!url) {
        resObj.code = 0;
        resObj.msg = "参数不全！"
        return resObj
    }
    if (!imgName) {
        resObj.code = 0;
        resObj.msg = "参数不全！"
        return resObj
    }
    if(username){
        //说明个人的更新  socket就只能让团队的更新
        resObj.team = false;
    }else{
        resObj.team = true;
    }
    let imgUrl = await saveImg(url,imgName);

    await model.changeProject(
        projectID,
        imgUrl,
        rotate,
        turn,
        opacity,
        light
    ).then((r) => {
        if (r <= 0) {
            resObj.code = 0;
            resObj.msg = "失败！"
        }
        console.log("changeProject_success:",resObj)
    }).catch((e) => {
        console.error(e);
        resObj.code = 500;
        resObj.msg = JSON.stringify(e)
        return resObj;
        res.sendStatus(500);
    })
    return resObj
}