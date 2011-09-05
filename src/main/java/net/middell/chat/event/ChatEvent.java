package net.middell.chat.event;

import net.middell.chat.ChatChannel;
import net.middell.chat.ChatMessage;
import net.middell.chat.ChatUser;
import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
public abstract class ChatEvent extends ApplicationEvent {
    private final ChatChannel channel;
    private final ChatUser user;
    private final ChatMessage message;

    protected ChatEvent(Object source, ChatChannel channel, ChatUser user, ChatMessage message) {
        super(source);
        this.channel = channel;
        this.user = user;
        this.message = message;
    }

    public ChatChannel getChannel() {
        return channel;
    }

    public ChatUser getUser() {
        return user;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
