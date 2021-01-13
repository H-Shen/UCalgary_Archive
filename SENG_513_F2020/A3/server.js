'use strict'

// Import libraries here
const app = require('express')();
const http = require('http').createServer(app);
const io = require('socket.io')(http);

// Define all constants here
const HISTORY_MAXITEMS = 200;
const PORT = 3000;
const MAX_LENGTH = 15;

const lightColors = [
    "ff3333",
    "ff6633",
    "ff9933",
    "ffcc33",
    "ffff33",
    "ccff33",
    "99ff33",
    "66ff33",
    "33ff33",
    "33ff66",
    "33ff99",
    "33ffcc",
    "33ffff",
    "33ccff",
    "3399ff",
    "3366ff",
    "3333ff",
    "6633ff",
    "9933ff",
    "cc33ff",
    "ff33ff",
    "ff33cc",
    "ff3399",
    "ff3366",
    "6acbfb",
    "6ec9f7",
    "1ab2ff",
    "ffffff",
    "edd59e"
];

// Define all global variables here
let listOfUsernames = new Map();
let chat_log_history = [];

// Add to the chat-log history
function addToChatLogHistory(time, username, usernameColor, message) {
    while (chat_log_history.length >= HISTORY_MAXITEMS) {
        chat_log_history.shift();
    }
    chat_log_history.push({time: time, username: username, usernameColor: usernameColor, message: message});
}

// Generate random username
function generateRandomUsername(length = 7) {
    let result = 'Guest_';
    const characters = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    const charactersLength = characters.length;
    for (let i = 0; i < length; ++i) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}

// Generate unique username
function generateRandomUniqueUsername() {
    while (true) {
        let result = generateRandomUsername();
        if (!listOfUsernames.has(result)) {
            return result;
        }
    }
}

// Generate a random color
function generateRandomLightColor() {
    return "#" + lightColors[Math.floor(Math.random() * lightColors.length)];
}

// Get current time from the timestamp
function getCurrentTime() {
    const now = new Date();
    const y = now.getFullYear();
    const m = now.getMonth() + 1;
    const d = now.getDate();
    let year = y;
    let month = (m < 10 ? "0" + m : m);
    let day_in_seconds = now.toTimeString().substr(0, 8);
    return year + "-" + month + "-" + (d < 10 ? "0" + d : d) + " " + day_in_seconds;
}

const isAlphaNumericUnderscore = ch => {
    return ch.match(/^[A-Za-z0-9_]+$/i) !== null;
}

function isValidColorHexCode(s) {
    return s.match(/^[a-fA-F0-9]{6}$/i) !== null;
}

function isValidName(s) {
    for (let i = 0; i < s.length; ++i) {
        if (!isAlphaNumericUnderscore(s[i])) {
            return false;
        }
    }
    return true;
}

function isNameValidLength(s) {
    return s.length <= MAX_LENGTH;
}

function updateChatLogHistoryWithNewName(newUsername, oldUsername) {
    for (let i = 0; i < chat_log_history.length; ++i) {
        if (chat_log_history[i].username === oldUsername) {
            chat_log_history[i].username = newUsername;
        }
    }
}

function updateChatLogHistoryWithNewColor(username, newUsernameColor) {
    for (let i = 0; i < chat_log_history.length; ++i) {
        if (chat_log_history[i].username === username) {
            chat_log_history[i].usernameColor = newUsernameColor;
        }
    }
}

function getPairs(myMap) {
    let result = [];
    for (const [k, v] of myMap) {
        result.push({name: k, color: v});
    }
    return result;
}

function replaceEmoji(s) {
    let result = "";
    // Link to the pictures in my github repo
    const emoji1 = "<img src=\"https://raw.githubusercontent.com/H-Shen/temp/master/e1.png\" alt=\"emoji1\" />";
    const emoji2 = "<img src=\"https://raw.githubusercontent.com/H-Shen/temp/master/e2.png\" alt=\"emoji2\" />";
    const emoji3 = "<img src=\"https://raw.githubusercontent.com/H-Shen/temp/master/e3.png\" alt=\"emoji3\" />";
    for (let i = 0; i < s.length;) {
        if (s[i] === ':') {
            if (i < s.length - 1) {
                if (s[i + 1] === ')') {
                    result += emoji1;
                    i += 2;
                } else if (s[i + 1] === '(') {
                    result += emoji2;
                    i += 2;
                } else if (s[i + 1] === 'o') {
                    result += emoji3;
                    i += 2;
                } else {
                    result = result + s[i];
                    ++i;
                }
            } else {
                result = result + s[i];
                ++i;
            }
        } else {
            result = result + s[i];
            ++i;
        }
    }
    return result;
}

app.get('/', (req, res) => {
    res.sendFile(__dirname + '/index.html');
});

io.on('connection', (socket) => {

    // generate the new username
    let username = generateRandomUniqueUsername();
    let usernameColor = generateRandomLightColor();
    listOfUsernames.set(username, usernameColor);

    // send the name and the color
    socket.emit('send name', username, usernameColor);

    socket.on('no need to check just update title and online list', () => {
        socket.emit('username in title', username, usernameColor);
        io.emit('user enters', getCurrentTime(), username, usernameColor, getPairs(listOfUsernames));
    });

    socket.on('send name back to check', (nameFromClient, colorFromClient) => {
        // if the client name is not in the map anymore, then the client can safely reuse its name
        // and we update the map in the server
        if (!listOfUsernames.has(nameFromClient)) {
            listOfUsernames.delete(username);
            username = nameFromClient;
            usernameColor = colorFromClient;
            listOfUsernames.set(username, usernameColor);
        }
            // Otherwise, the client name is occupied by another client, then we notify the client
        // to user and the new username and the new username color
        else {
            socket.emit('resend name', username, usernameColor);
        }
        // change the client's title
        socket.emit('username in title', username, usernameColor);
        // notify all users a new user enters the chat room and update the list of online users
        io.emit('user enters', getCurrentTime(), username, usernameColor, getPairs(listOfUsernames));
    });


    // send the chat-log history to the client
    socket.emit('get chat log history', chat_log_history);

    socket.on('disconnect', () => {
        // remove its username from the hashmap
        listOfUsernames.delete(username);
        // notify all users a user leaves the chat room
        io.emit('user leaves', getCurrentTime(), username, usernameColor, getPairs(listOfUsernames));
    });

    socket.on('message', (msg) => {
        // Case 1: Command mode
        if (msg.length > 0 && msg[0] === '/') {
            // parse the message
            let commands = msg.split(" ");
            if (commands[0] === "/name") {
                // subcase 1:
                if (commands.length === 1) {
                    socket.emit('invalid_name_1');
                }
                // subcase 2: the name contains invalid characters
                else if (commands.length > 2 || !isValidName(commands[1])) {
                    socket.emit('invalid_name_2');
                }
                // subcase 3: the name is too long
                else if (!isNameValidLength(commands[1])) {
                    socket.emit('invalid_name_3');
                }
                // subcase 4: the name is unchanged
                else if (commands[1] === username) {
                    socket.emit('invalid_name_4');
                }
                // subcase 5: the name is duplicated
                else if (listOfUsernames.has(commands[1])) {
                    socket.emit('invalid_name_5');
                }
                // subcase 6: successfully change the name
                else {
                    let oldUsername = username;
                    let usernameColorBackup = listOfUsernames.get(oldUsername);
                    listOfUsernames.delete(oldUsername);
                    username = commands[1];
                    listOfUsernames.set(username, usernameColorBackup); // change the key, restore the value
                    socket.emit('valid_name_change', username);
                    // Update the client's title
                    socket.emit('username in title', username, usernameColor);
                    // Update the chat-log history
                    updateChatLogHistoryWithNewName(username, oldUsername);
                    // Ask all users update their local chat-log
                    io.emit('update local chat log username', username, oldUsername);
                    // Ask all users update their lists of online users
                    io.emit('update the list of online users', getPairs(listOfUsernames));
                }
            } else if (commands[0] === "/color") {
                // subcase 1:
                if (commands.length === 1) {
                    socket.emit('invalid_color_1');
                }
                // subcase 2:
                else if (commands.length > 2 || !isValidColorHexCode(commands[1])) {
                    socket.emit('invalid_color_2');
                }
                // subcase 3:
                else if ('#' + commands[1].toUpperCase() === usernameColor) {
                    socket.emit('invalid_color_3');
                }
                // subcase 4:
                else {
                    usernameColor = '#' + commands[1].toUpperCase();
                    listOfUsernames.set(username, usernameColor);
                    socket.emit('valid_color_change', usernameColor);
                    // Update the client's title
                    socket.emit('username in title', username, usernameColor);
                    // Update the chat-log history
                    updateChatLogHistoryWithNewColor(username, usernameColor);
                    // Ask all users update their local chat-log
                    io.emit('update local chat log username color', username, usernameColor);
                    // Ask all users update their lists of online users
                    io.emit('update the list of online users', getPairs(listOfUsernames));
                }
            } else {
                socket.emit('invalid_command');
            }
        }
        // Case 2: Conversation mode
        else {
            // emit the message to all sockets
            let currentTime = getCurrentTime();
            io.emit('message', currentTime, username, usernameColor, replaceEmoji(msg));
            // store to the chat-log history
            addToChatLogHistory(currentTime, username, usernameColor, replaceEmoji(msg));
        }
    });
});

http.listen(PORT, () => {
    console.log('listening on *:' + PORT);
});