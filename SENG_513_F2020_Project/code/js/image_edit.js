//require("./foto.js");
//const chatjs = require("./code.js");

//import * as Foto from "./foto.mjs"
//import * as chattyBox from "./code.mjs";

window.onclick = function (event) {
    if (!event.target.matches('.dropbtn')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}

// toggle showing and hiding dropdown
function toggleDropdown (elem) {
    $('.show').removeClass('show');
    $(elem).next().toggleClass("show");
}

window.onload = function (){
    $.ajax({
        type: "post",
        url: window.location.origin+"/check_session",
        //server sent back response
        success: (res) => {
            switch (res["ret"]) {
                case 1:
                    break;
                case -1:
                    alert('You didnt Login, bro. Back to the login page.');
                    window.location.href = 'index.html';
                    break;
            }
        },
        error: () => {
            console.log('process error');
        }
    });
    $.ajax({
        type: "get",
        url: window.location.origin+"/user_profile",

        //server sent back response
        success: (res) => {
            switch (res.ret.returnCode) {
                case 1:
                    $('#username').text(res.ret.userInfo.username);
                    $('#user-email').text(res.ret.userInfo.user_email);
                    break;
                case -1:
                    alert("The system is down.");
                    break;
            }
        },
        error: () => {
            console.log('process error');
        }
    });
    //}
    $('#logout').click(
        function test2() {
            $.ajax({
                type: "post",
                url: window.location.origin+"/delete_session",
                //server sent back respons
                success: (res) => {
                    switch (res["ret"]) {
                        case 1:
                            window.location.href = 'index.html';
                            break;
                        case -1:
                            $.ajax({
                                type: "post",
                                url: window.location.origin+"/check_session",
                                //server sent back response
                                success: (res) => {
                                    switch (res["ret"]) {
                                        case 1:
                                            alert("The server is down...")
                                            break;
                                        case -1:
                                            window.location.href = 'index.html';
                                            break;
                                    }
                                }
                            });
                            break;
                    }
                }
            })
        }

    );
}

