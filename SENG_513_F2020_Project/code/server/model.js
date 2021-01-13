const mysql = require('mysql2');



let localhost_mysql_config = {
    host: '127.0.0.1',
    user: 'root',
    password: 'root',
    database: 'seng_513_project',
    port: 3306,
    multipleStatements: true
}



let produtDev_mysql_config = {
    host: '35.230.37.124',
    user: 'newuser',
    password: 'piALj0WZBoB+p7vuRfHh96aEjrVdBscDW8CuEgrQ',
    database: 'seng_513_project',
    port: 3306,
    multipleStatements: true

}

// change
const DATABASE_LOGIN_INFO = localhost_mysql_config;



exports.getPassByUser = async function (username) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    // query database
    const [rows, field] = await connection.execute('select password from seng_513_project.user where `username` = ?', [username]);
    await connection.end(); // 关闭通信
    return rows[0];
}

/*
    addUserInfo()
    TODO args:username,password,email,question,answer
    1:  insert return = 1 : successful
    2:  Other cases : error.
*/

exports.addUserInfo = async function (username, password, email, question, answer) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    let sql = `
    insert into seng_513_project.user  (username, email, password, password_question, answer_of_password_question) 
    SELECT '${username}', '${email}', '${password}','${question}', '${answer}' from user
    where not exists (select * from user  where username = "${username}" ) LIMIT 1
    `
    console.log("addUserInfo---sql:",sql);
    const [rows, field] = await connection.execute(sql);
    await connection.end(); // 关闭通信
    return rows["affectedRows"];
}

/*
    getUserInfo()
    TODO args:username
    1:  select return json
    2:  Other cases : empty or other error.
*/
exports.getUserInfo = async function (username) {    // get the client
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    // query database
    const [rows, field] = await connection.execute('select * from seng_513_project.user where `username` = ?', [username]);
    await connection.end(); // 关闭通信
    return rows[0];
}

// TODO args:username
// 1. get and return data (team info : teams and its members)
// 1.5 . return {rows, fields}
// 2. Other case : empty or other error
/*
    /get_Info
    Params:
    Return: JSON
    Success:
    {returnCode:1,info:{teams:[teams],members:{team1:[members],team2:[members]}}
    Failed:
    {returnCode: -1}
*/
exports.getTeamInfo = async function () {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    // query database
    const [rows, field] = await connection.execute('select a.code, b.username from seng_513_project.team as a, seng_513_project.user as b where (a.uuid, b.uuid) in (select seng_513_project.team2members.team_uuid, seng_513_project.team2members.member_uuid from seng_513_project.team2members);');

    let info = { teams: [], members: {} };

    // obtain teams:[teams]
    let set_of_teamcode = new Set();
    for (let i of rows) {
        set_of_teamcode.add(i['code']);
    }
    info["teams"] = Array.from(set_of_teamcode);
    for (let i of info["teams"]) {
        info["members"][i] = [];
    }
    for (let i of rows) {
        info["members"][i["code"]].push(i["username"]);
    }
    await connection.end(); // 关闭通信
    return info;
    // returncode不知道 info已经处理完毕 需后端自行处理
}

// TODO args: username
// 1. add a team. with an team ID produced by Service layer and the user's username
// return: : affectRows
// 2. Other case : empty or other error

exports.addTeam = async function (code) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    // query database
    const [rows, field] = await connection.execute('insert into seng_513_project.team values (UUID(), ?)', [code]);
    await connection.end(); // 关闭通信
    return rows["affectedRows"];
}

exports.addTeamMember = async function (username, code) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    // query database
    const [rows, field] = await connection.execute('insert into seng_513_project.team2members values ((select seng_513_project.team.uuid from seng_513_project.team where team.code = ? limit 1), (select seng_513_project.user.uuid from seng_513_project.user where user.username = ? limit 1));', [code, username]);
    await connection.end(); // 关闭通信
    return rows["affectedRows"];
}

exports.deleteMemberFromTeam = async function (username, code) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    // query database
    const [rows, field] = await connection.execute('delete from seng_513_project.team2members where seng_513_project.team2members.team_uuid in (select seng_513_project.team.uuid from seng_513_project.team where seng_513_project.team.code = ?) and seng_513_project.team2members.member_uuid in (select seng_513_project.user.uuid from seng_513_project.user where seng_513_project.user.username = ?);', [code, username]);
    await connection.end(); // 关闭通信
    return rows[0]; //不确定这里的返回值
}

exports.deleteTeamPic = async function (team_code) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    // query database
    const [rows, field] = await connection.execute('delete from seng_513_project.team_pic where seng_513_project.team_pic.team_uuid in (select seng_513_project.team.uuid from seng_513_project.team where seng_513_project.team.code = ?);', [team_code]);
    await connection.end(); // 关闭通信
    return rows[0]; //不确定这里的返回值
}

exports.deleteTeamComments = async function (team_code) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    // query database
    const [rows, field] = await connection.execute('delete from seng_513_project.comment where seng_513_project.comment.team_uuid in (select seng_513_project.team.uuid from seng_513_project.team where seng_513_project.team.code = ?);', [team_code]);
    await connection.end(); // 关闭通信
    return rows[0]; //不确定这里的返回值
}

exports.deleteTeamFromMember = async function (username) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    // query database
    const [rows, field] = await connection.execute('delete from seng_513_project.team2members where seng_513_project.team2members.member_uuid in (select seng_513_project.user.uuid from seng_513_project.user where seng_513_project.user.username = ?);', [username]);
    await connection.end(); // 关闭通信
    return rows[0]; //不确定这里的返回值
}

exports.deleteTeam = async function (team_code) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    // query database
    const [rows, field] = await connection.execute('delete from seng_513_project.team where seng_513_project.team.code = ?', [team_code]);
    await connection.end(); // 关闭通信
    return rows[0]; //不确定这里的返回值
}

exports.getTeamsByUsername = async function (username) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    // query database
    const [rows, field] = await connection.execute('select team.code from seng_513_project.team where team.uuid in (select team2members.team_uuid from seng_513_project.team2members where team2members.member_uuid in (select user.uuid from seng_513_project.user where user.username = ?));', [username]);
    let teams = [];
    for (let i of rows) {
        teams.push(i["code"]);
    }
    await connection.end(); // 关闭通信
    return teams;
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


/*
首先给团队增加一个分组
*/

exports.createGroup = async function (teamID, memberID) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    let sql = `insert into seng_513_project.team  (name,member,teamID,project) values ("${teamID}","${memberID}","${teamID}","")`;
    const [rows, field] = await connection.execute(sql);
    await connection.end(); // 关闭通信
    return rows["affectedRows"];
}



/*
查询分组
*/

exports.queryGroups = async function (teamID) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    let sql = `select * from team`;
    if (teamID) {
        sql += ` where teamID = ${teamID} `
    }
    const [rows, field] = await connection.execute(sql);
    await connection.end(); // 关闭通信
    return rows;
}


/*
查询成员
*/

exports.queryMembersById = async function (memeberID) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    let sql = `select * from user`;
    if (memeberID) {
        sql += ` where username in(${memeberID}) `;
    }
    const [rows, field] = await connection.execute(sql);
    await connection.end(); // 关闭通信
    return rows;
}




/*
查询项目
*/

exports.queryProjectById = async function (str) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    let sql = `select * from project`;
    if (str) {
        sql += ` where projectID in(${str}) `;
    }
    const [rows, field] = await connection.execute(sql);
    await connection.end(); // 关闭通信
    return rows;
}




/*
离开分组
*/

exports.leaveOrJoinGroup = async function (teamID, memberID) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);

    let sql = `UPDATE team SET member = "${memberID}" WHERE teamID = ${teamID}`;
    if (!memberID) {
        //没得团队成员 那么就删除该团队
        sql = `DELETE FROM team WHERE teamID = ${teamID}`;
    }
    const [rows, field] = await connection.execute(sql);
    await connection.end(); // 关闭通信
    return rows["affectedRows"];
}


/*
添加项目
*/

exports.addProjectS = async function (projectID, url, rotate, turn, opacity, light, teamID, username, teamProjectID, userProjectID) {
    console.log("addProjectS--projectID:", projectID)
    // console.log("addProjectS--url:",url)

    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);
    const connection2 = await mysql.createConnection(DATABASE_LOGIN_INFO);

    let sql = `insert into seng_513_project.project  (projectID,url,rotate,turn,opacity,light) values ("${projectID}","${url}","${rotate ? rotate : ''}","${turn ? turn : ''}","${opacity ? opacity : ''}","${light ? light : ''}");`;
    let sql2 = "";
    if (teamID) {
        let teamProjectIDS = "";
        if (teamProjectID) {
            teamProjectIDS = teamProjectID + "," + projectID;
        } else {
            teamProjectIDS = projectID
        }
        sql2 = `UPDATE team SET project='${teamProjectIDS}' WHERE teamID = '${teamID}';`
    }
    if (username) {
        let userProjectIDS = "";
        if (userProjectID) {
            userProjectIDS = userProjectID + "," + projectID;
        } else {
            userProjectIDS = projectID
        }
        sql2 = `UPDATE \`user\` SET project='${userProjectIDS}' WHERE \`username\` = '${username}' ;`
    }
    console.log("addProjectS---sql:", sql)
    console.log("addProjectS---sql2:", sql2)

    const [rows, field] = await connection.execute(sql);
    const [rows2, field2] = await connection2.execute(sql2);

    await connection.end(); // 关闭通信
    await connection2.end(); // 关闭通信
    console.log("addProjectS---rowsaffectedRows:", rows["affectedRows"])
    console.log("addProjectS---rows2----saffectedRows:", rows2["affectedRows"])
    if (rows["affectedRows"] > 0 && rows2["affectedRows"] > 0) {
        return 1;
    }
    return 0;
}


/*
修改项目
*/

exports.changeProject = async function (projectID, url, rotate, turn, opacity, light) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);

    let sql = `UPDATE project SET url = "${url}" `;

    if (rotate) {
        sql += `  ,rotate = "${rotate}" `;
    }
    if (turn) {
        sql += `  ,turn = "${turn}" `;
    }
    if (opacity) {
        sql += `  ,opacity = "${opacity}" `;
    }
    if (light) {
        sql += `  ,light = "${light}" `;
    }

    sql += `  WHERE projectID = ${projectID}`;
    const [rows, field] = await connection.execute(sql);
    await connection.end(); // 关闭通信
    return rows["affectedRows"];
}



/**
 * 修改密码
 * 
 */

exports.changeUserInfo = async function (username, password) {
    // get the client
    const mysql = require('mysql2/promise');
    const connection = await mysql.createConnection(DATABASE_LOGIN_INFO);

    let sql = `UPDATE user SET password = "${password}" `;
    sql += `  WHERE username = '${username}'`;
    const [rows, field] = await connection.execute(sql);
    await connection.end(); // 关闭通信
    return rows["affectedRows"];
}
