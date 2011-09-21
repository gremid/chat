<#ftl>
<#assign title = (member.nickName + "@" + channel.name)?html>
<@ch.page title>

<div style="margin: 0 auto; width: 850px">
    <div id="bot-control">
        <button id="run-bot">Run</button>
        <button id="stop-bot">Stop</button>
    </div>

    <div id="bot-area" class="yui3-g">
        <div class="yui3-u-1-3">
            <div id="bot-code"></div>
        </div>
        <div class="yui3-u-2-3">
            <div id="bot-editor"><pre>/*
 * Viel Spaß beim Programmieren eines Bots!
 *
 * Verfügbare Variablen:
 *
 *   sender: Absender der empfangenen Nachricht
 *   message: Empfangene Nachricht
 *   timestamp: Zeitstempel der empfangenen Nachricht
 */
say("Hello World!");</pre></div>
        </div>
    </div>

    <div class="yui3-g" id="chat-area">
        <div class="yui3-u-1-5">
            <div id="member-list"></div>
        </div>
        <div class="yui3-u-4-5" id="messages">
            <div id="chat-window"></div>
            <div id="message-entry">
                <form>
                    <input type="text" id="message" name="message" rows="2">
                    <br><input type="submit" value="Senden">
                </form>
            </div>
        </div>
    </div>

    <div id="bot-errors"></div>
</div>

<script type="text/javascript">
    var nickname = "${member.nickName?js_string}";
    var uid = "${member.id?js_string}";
    var channel = "${channel.name?js_string}";

    YUI().use("chat", "node", "dump", "transition", function(Y) {
        Y.on("domready", function() {
            var botEditor = ace.edit("bot-editor"),
                    memberList = Y.one("#member-list"), chatWindow = Y.one("#chat-window"), messageInput = Y.one("#message"),
                    botCode = Y.one("#bot-code"), botErrors = Y.one("#bot-errors"),
                    memberUpdate, messageUpdate, bot,
                    lastReceived = 0, lastSent = 0, botMessageThreshold = 5;

            function updateMembers() {
                Y.chat.members(channel, function(ml) {
                    memberList.setContent("");
                    Y.each(ml, function(m) {
                        memberList.append(Y.Node.create("<p/>").addClass("member").set("text", m.n));
                    });
                });
            }

            function pullMessages() {
                Y.chat.messages(channel, lastReceived, 20, function(ml) {
                    Y.all(".unconfirmed").remove();
                    Y.each(ml, function(m) {
                        lastReceived = m.ts;
                        addMessage(m);
                    });
                    if (bot) {
                        Y.each(ml, function(m, i) {
                            if ((m.s.n != nickname)) {
                                try {
                                    bot(m.s.n, m.msg, m.ts);
                                } catch (e) {
                                    botErrors.append(Y.Node.create("<p/>").set("text", e.message));
                                }
                            }
                        });
                    }
                });
            }

            function addMessage(m, clazz) {
                var existing = chatWindow.get("children");
                if (existing.size() == 20) chatWindow.removeChild(existing.shift());

                var messagePara = Y.Node.create("<p/>")
                        .addClass("message")
                        .addClass(clazz || "confirmed")
                        .append(Y.Node.create("<span/>")
                        .addClass("sender")
                        .set("text", m.s.n))
                        .append(Y.Node.create("<span/>")
                        .addClass("content")
                        .set("text", m.msg));
                chatWindow.append(messagePara);
                messagePara.scrollIntoView();
            }

            function say(messageContent) {
                messageContent = messageContent.toString().replace("^\s+", "").replace("\s+$", "");
                if (messageContent.length == 0) return;

                var now = Date.now();
                if ((now - lastSent) >= 2000) {
                    lastSent = now;
                    Y.chat.send(channel, uid, messageContent, function(m) { addMessage(m, "unconfirmed"); });
                }
            }

            function sendMessage(e) {
                e.preventDefault();
                say(messageInput.get("value"));
                messageInput.set("value", "");
                messageInput.focus();
            }

            function runBot(e) {
                try {
                    var botCodeContent = botEditor.getSession().getValue();
                    eval("bot = function (sender, message, timestamp) {" + botCodeContent + "}");
                    var codePre = Y.Node.create("<pre/>")
                            .set("text", botCodeContent);
                    botCode.insert(codePre, "replace");
                    botCode.show(true);
                } catch (e) {
                    alert("Fehler bei der Interpretation des Bot-Codes; bitte überprüfen Sie die Syntax.");
                    stopBot(e);
                }
            }

            function stopBot(e) {
                bot = undefined;
                botCode.hide(true);
            }

            window.onunload = function (e) {
                if (memberUpdate) memberUpdate.cancel();
                if (messageUpdate) messageUpdate.cancel();
                Y.chat.leave(channel, uid);
            };

            Y.chat.join(channel, uid, function(result) {
                updateMembers();
                memberUpdate = Y.later(3000, null, updateMembers, null, true);

                pullMessages();
                messageUpdate = Y.later(3000, null, pullMessages, null, true);

                Y.on("submit", sendMessage, "#message-entry form");

                botEditor.setTheme("ace/theme/solarized_light");
                botEditor.renderer.setHScrollBarAlwaysVisible(false);
                var JavaScriptMode = require("ace/mode/javascript").Mode;
                botEditor.getSession().setMode(new JavaScriptMode());

                botCode.hide();

                Y.one("#message").focus();
                Y.on("click", runBot, "#run-bot");
                Y.on("click", stopBot, "#stop-bot")

            });
        });
    });
</script>
</@ch.page>