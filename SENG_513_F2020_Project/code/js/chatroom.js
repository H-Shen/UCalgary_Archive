/* 聊天室的主要功能 */
// 1.连接socketio服务
let localUser = null;
let teamID = "";

init();
function init() {
  localUser = sessionStorage.getItem("username");
  teamID = getQueryVariable("teamID");
  socket.emit('login', { username: localUser, page: "group-project-edit", teamID })
  queryMembers();
}

socket.on('updateUsers', () => {
  queryMembers();
})

function updateMembers(mbs) {
  let str = "";
  for (let item of mbs) {
    const row = `
      <div class="group-user" data-name="${item}" title="${item}">
        <div class="user_name">${item}</div>
      </div>
      `
    str += row;
  }
  $('.users').html(str)
}
socket.on("leaveOrJoinGroup", () => {
  queryMembers(globalObj.activeTeamID);
})
function queryMembers() {
  request("/queryGroups", { teamID }).then((res) => {
    let { member } = res.data[0];
    const members = member.split(",");
    updateMembers(members)
  })
}
socket.on('updateMessage', message => {
  $('.chat-msg').html("")
  message.map(item => { //所有聊天记录
    dealMessag(item)
  })
})

socket.on('updateWatcher', watcher => {
  const nodes = $('.group-user');
  for (const node of nodes) {
    const name = node.dataset.name;
    if(watcher.includes(name)){
      $(node).addClass("watch")
    }else{
      $(node).removeClass("watch")
    }
  }
})

socket.on('someoneLogin', user => {
  let str = `
    <div class="tips">${user}加入了群聊</div>
  `;
  $('.chat-msg').append(str)
  scrollIntoView()
  console.log("someoneLogin")
  getProject();//只要有人进来就全部设置成默认
})

socket.on('someoneLeave', user => {
  let str = `
    <div class="tips">${user.name}离开了群聊</div>
  `;
  $('.chat-msg').append(str)
  scrollIntoView()
})

$('#btn-send').on('click', function () {
  sendMsg();
})

function sendMsg() {
  const msg = $('#msg').val();
  if (!msg) {
    return;
  }
  socket.emit('messageUp', {
    msg,
    user: localUser,
    teamID
  })
  $('#msg').val("");
}

//监听按键
$(window).keydown(function (event) {
  // 监听esc键退出全屏
  const { code } = event;
  if (code === 'NumpadEnter' || code === 'Enter') {
    event.preventDefault();
    sendMsg();
  }
});

socket.on('messageDown', msg => {
  dealMessag(msg);
})

function dealMessag(msg) {
  const cls = msg.name == localUser ? "self" : "";
  let message = msg.msg;
  let str = `
    <div class="msg ${cls}">
      <div class="msg-info">[${msg.name}]${message}</div>
      <div class="time">${msg.time}</div>
    </div>
  `;
  $('.chat-msg').append(str)
  scrollIntoView()
}

// 当有消息时，将滑动到底部
function scrollIntoView() {
  // 当前元素的底部滚动到可视区
  $('.chat-msg').children(':last').get(0).scrollIntoView(false)
}