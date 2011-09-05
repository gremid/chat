package net.middell.chat.event;

import net.middell.chat.ChatChannel;
import net.middell.chat.ChatService;
import net.middell.chat.ChatUser;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
public class ChatChannelOpenedEvent extends ChatEvent {

    public ChatChannelOpenedEvent(ChatService chatService, ChatChannel channel, ChatUser creator) {
        super(chatService, channel, creator, null);
    }
}
