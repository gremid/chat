<#ftl>
<@ch.page "Start">

<div class="yui3-g" id="start-panel">
    <div class="yui3-u-1-2">
        <div id="channel-list"></div>
    </div>
    <div class="yui3-u-1-2">
        <div id="channel-control-panel">
            <form>
                <div class="yui3-g">
                    <div class="yui3-u-1-3 form-label"><label for="nickname">Namenskürzel:</label></div>
                    <div class="yui3-u-2-3"><input type="text" id="nickname" name="nickname" value=""></div>
                </div>
                <div class="yui3-g">
                    <div class="yui3-u-1-3 form-label"><label for="channel">Chat-Raum:</label></div>
                    <div class="yui3-u-2-3"><input type="text" id="channel" name="channel" value=""></div>
                </div>
                <div class="form-control">
                    <input type="submit" value="Chat-Raum betreten">
                </div>
            </form>
        </div>
    </div>
</div>


<script type="text/javascript">
    YUI().use("chat", "node", "dump", function(Y) {
        function updateChannelList() {
            Y.chat.channels(function(cl) {
                var channelListContainer = Y.one("#channel-list");
                channelListContainer.setContent("");

                if (cl.length == 0) {
                    channelListContainer.insert('<p class="notice">Momentan sind keine Chaträume eingerichtet. Warum eröffnen Sie nicht einen.</p>')
                    return;
                }

                Y.each(cl, function(c) { c.value = c.m * 0.5; });

                function selectChannel(channel) {
                    Y.one("#channel").set("value", channel);
                    Y.one("#nickname").focus();
                }

                var r = 400, format = d3.format(",d");

                var bubble = d3.layout.pack().sort(null).size([r, r]);

                var vis = d3.select("#channel-list").append("svg:svg")
                        .attr("width", r)
                        .attr("height", r)
                        .attr("class", "bubble");

                var node = vis.selectAll("g.node")
                        .data(bubble.nodes({children: cl})
                        .filter(function(d) {
                            return !d.children;
                        }))
                        .enter().append("svg:g")
                        .attr("class", "node")
                        .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

                node.append("svg:title").text(function(d) {
                    return "'" + d.ch + "' (" + format(d.m) + " Teilnehmer)";
                });

                node.append("svg:circle")
                        .attr("r", function(d) { return d.r; })
                        .style("fill", "#d3d3d3")
                        .on("click", function(d) { selectChannel(d.ch); });

                node.append("svg:text")
                        .attr("text-anchor", "middle")
                        .attr("dy", ".3em")
                        .text(function(d) { return d.ch; })
                        .on("click", function(d) { selectChannel(d.ch); });
            });
        }

        Y.on("submit", function(e) {
            e.preventDefault();
            var nicknameInput = Y.one("#nickname");
            var nickname = nicknameInput.get("value").replace(/\/\s+/g, " ");
            if (nickname.length == 0) {
                alert("Bitte geben Sie ein Namenskürzel an!");
                nicknameInput.addClass("form-error");
                nicknameInput.focus();
                return;
            } else {
                nicknameInput.removeClass("form-error");
            }

            var channelInput = Y.one("#channel");
            var channel = channelInput.get("value").replace(/\/\s+/g, " ");
            if (channel.length == 0) {
                alert("Bitte geben Sie den zu betretenden Chat-Raum an!");
                channelInput.addClass("form-error");
                channelInput.focus();
                return;
            } else {
                channelInput.removeClass("form-error");
            }

            Y.chat.login(nickname, function(uid) {
                window.location.pathname = cp + "/channel/" + encodeURIComponent(channel) + "/" + encodeURIComponent(uid);
            }, function() {
                alert("Anmeldung fehlgeschlagen; bitte vesuchen Sie es mit einem anderen Namenskürzel.");
                nicknameInput.addClass("form-error");
                nicknameInput.focus();
            });
        }, "#channel-control-panel form");

        updateChannelList();
        Y.later(5000, null, updateChannelList, null, true);

        Y.one("#nickname").focus();

    });
</script>
</@ch.page>