package net.middell.chat.event;

import net.middell.chat.ChatChannel;
import net.middell.chat.ChatService;
import net.middell.chat.ChatUser;
import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
public class ChatUserJoinedChannel extends ChatEvent {
    public ChatUserJoinedChannel(Object source, ChatChannel channel, ChatUser member) {
        super(source, channel, member, null);
    }
}
