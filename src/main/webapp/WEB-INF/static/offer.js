var organizer = null;

YUI().use("dump", "node", "substitute", "autocomplete", "autocomplete-highlighters", function(Y) {
    Y.on("domready", function() {
        Y.one("#title").focus();

        var organizerInput = Y.one("#organizer");
        var organizerAutoComplete = new Y.AutoComplete({
            inputNode: organizerInput,
            source: cp + "/organizer/search/{query}",
            resultHighlighter: function(query, results) {
                return Y.Array.map(results, function(result) {
                    var o = result.raw;
                    return Y.Highlight.all(Y.substitute("({iata}) {name} ({merlinCode}/ {traveltainmentCode})", o, function(k, v) {
                        return (v == null ? "–" : v);
                    }), query);
                });
            },
            render: true
        });

        Y.on("blur", function(e) {
            if (organizer != null && (this.get("value") != organizer.name)) {
                organizer = null;
            }
            if (organizer == null) {
                this.set("value", "");
            }
        }, organizerInput);

        organizerAutoComplete.on("select", function(e) {
            organizer = e.result.raw;
            Y.log(Y.dump(organizer));
            e.result.text = organizer.name;
        });

        Y.on("submit", function(e) {
            e.preventDefault();
        }, "#offer-form");
    });
});