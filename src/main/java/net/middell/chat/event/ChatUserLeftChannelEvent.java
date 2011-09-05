package net.middell.chat.event;

import net.middell.chat.ChatChannel;
import net.middell.chat.ChatService;
import net.middell.chat.ChatUser;
import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
public class ChatUserLeftChannelEvent extends ChatEvent {
    public ChatUserLeftChannelEvent(Object source, ChatChannel channel, ChatUser user) {
        super(source, channel, user, null);
    }
}
