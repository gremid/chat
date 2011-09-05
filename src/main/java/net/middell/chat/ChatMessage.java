package net.middell.chat;

import com.google.common.base.Objects;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
public class ChatMessage implements Comparable<ChatMessage> {
    private final long timestamp;
    private final ChatUser sender;
    private final String content;

    public ChatMessage(ChatUser sender, String content) {
        this.timestamp = System.currentTimeMillis();
        this.sender = sender;
        this.content = content;
    }

    @JsonProperty("ts")
    public long getTimestamp() {
        return timestamp;
    }

    @JsonProperty("s")
    public ChatUser getSender() {
        return sender;
    }

    @JsonProperty("msg")
    public String getContent() {
        return content;
    }

    @Override
    public int compareTo(ChatMessage o) {
        final int timeDiff = (int) (timestamp - o.timestamp);
        return (timeDiff == 0) ? sender.compareTo(o.sender) : timeDiff;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("ts", timestamp)
                .add("sender", sender)
                .addValue(content)
                .toString();
    }
}
