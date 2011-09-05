YUI.add("chat", function(Y) {
    Y.namespace("chat");

    function errorHandler() {
        alert("Bei der Kommunikation mit dem Chat-Server trat ein Fehler auf.\n" +
            "Ggf. waren Sie zu lange inaktiv oder die Netzverbindung wurde unterbrochen.\n" +
            "Sie werden zur Anmeldeseite umgeleitet.");
        window.location.pathname = cp + "/";
    }

    function json(url, cb, method) {
        Y.io(cp + url, {
            method: method || "get",
            headers: {
                    "Accept": "application/json"
                },
            on: {
                    success: function(transactionId, resp) {
                        if (resp.status == 200) cb(Y.JSON.parse(resp.responseText));
                        else errorHandler();
                    },
                    failure: errorHandler
                }
            });
    }

    var enc = encodeURIComponent;

    Y.chat.login = function(nickname, successCb, failureCb) {
        Y.io(cp + "/user/login/" + enc(nickname), {
            method: "post",
            headers: {
                    "Accept": "application/json"
                },
            on: {
                    success: function(transactionId, resp) {
                        if (resp.status == 200) successCb(Y.JSON.parse(resp.responseText).id);
                        else errorHandler();
                    },
                    failure: failureCb
                }
            });
    };
    Y.chat.logout = function(uid) { json("/user/logout/" + enc(uid), function() {}, "post"); };

    Y.chat.channels = function(cb) { json("/channel/", cb); };
    Y.chat.members = function(channel, cb) { json("/channel/" + enc(channel), cb); };
    Y.chat.join = function(channel, uid, cb) { json("/channel/" + enc(channel) + "/" + enc(uid), cb, "post"); };
    Y.chat.leave = function(channel, uid) {
        Y.io(cp + "/channel/" + enc(channel) + "/" + enc(uid), {
            method: "delete",
            sync: true,
            headers: { "Accept": "application/json" }
            });

    };

    Y.chat.messages = function(channel, lastTs, max, cb) { json("/message/" + enc(channel) + "?l=" + enc(lastTs) + "&m=" + enc(max), cb); };
    Y.chat.send = function(channel, uid, msg, cb) {
            Y.io(cp + "/message/" + enc(channel) + "/" + enc(uid), {
            method: "post",
            data: msg,
            headers: {
                    "Accept": "application/json",
                    "Content-Type": "text/plain;charset=utf-8"
                },
            on: {
                    success: function(transactionId, resp) {
                        if (resp.status == 200) cb(Y.JSON.parse(resp.responseText));
                        else errorHandler();
                    },
                    failure: errorHandler
                }
            });
    };


}, "0", {
    requires: ["io", "json"]
});