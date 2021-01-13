$(function () {
    var user;
    var socket = io();
    
    $('#send_submit').click(function(e){
      e.preventDefault(); // prevents page reloading
      $('#userinput').val((index, value) => {return value.replace(':)', '😁'); })
      $('#userinput').val((index, value) => {return value.replace(':(', '🙁'); })
      $('#userinput').val((index, value) => {return value.replace(':o', '😲'); })
      socket.emit('chat message', $('#userinput').val());
      $('#messages').stop(true, true).animate({scrollTop: $('#messages')[0].scrollHeight}, 1000);
      $('#userinput').val('');
      return false;
    });

    socket.on('if first time user', () => {
        document.cookie != "" ? socket.emit('returningUser', document.cookie): socket.emit('firstTimeUser');
    });

    socket.on('replace message log', (msgLog) => {
        // $('#usernameList').html('');
        $('#messages').html('');

        for (var i=0; i<msgLog.length; i++) {
            if (msgLog[i].name === user)
                { $('#messages').append($('<li>').html(`<b>${msgLog[i].timestamp}<span style="color:${msgLog[i].color}">${msgLog[i].name}</span>: ${msgLog[i].msg}</b>`)); }
            else 
                { $('#messages').append($('<li>').html(`${msgLog[i].timestamp}<span style="color:${msgLog[i].color}">${msgLog[i].name}</span>: ${msgLog[i].msg}`)); }
                // $('#messages').stop(true, true).animate({scrollTop: $('#messages')[0].scrollHeight}, 1000);
                // if ($('#messages').height() >= $('.chatWindow').height() - 26) {
                //     $('#messages').css("height", "calc(100%-25px)");
                //     $('#messages').css("overflow-y", "auto");
                // };
        }
        let scw = document.getElementById("scrollWindow");
        scw.scrollTop = scw.scrollHeight;
    });

    socket.on('cookieSaved', (name, color) => {
        user = name;
        document.cookie = name + color;
    });

    socket.on('chat message', (timestamp, color, name, msg) => {
        $('#messages').append($('<li>').html(`${timestamp}<span style="color:${color}">${name}</span>: ${msg}`));
        // $('#messages').animate({
        //     scrollTop: $('#messages').prop('scrollHeight')
        // }, 1000);
        let scw = document.getElementById("scrollWindow");
        scw.scrollTop = scw.scrollHeight;
        // if ($('#messages').height() >= $('.chatWindow').height() - 26) {
        //     //$('#messages').css("height", "calc(100%-25px)");
        //     //$('#messages').css("overflow-y", "auto");
        // };
    });

    socket.on('bold message', (timestamp, color, name, msg) => {
        $('#messages').append($('<li>').html(`<b>${timestamp}<span style="color:${color}">${name}</span>: ${msg}</b>`));
        // if ($('#messages').height() >= $('.chatWindow').height() - 26) {
        //     $('#messages').css("height", "calc(100%-25px)");
        //     $('#messages').css("overflow-y", "auto");
        // };
        let scw = document.getElementById("scrollWindow");
        scw.scrollTop = scw.scrollHeight;
    });
});