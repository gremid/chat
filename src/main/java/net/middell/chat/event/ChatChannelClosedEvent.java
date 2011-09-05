package net.middell.chat.event;

import net.middell.chat.ChatChannel;
import net.middell.chat.ChatService;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
public class ChatChannelClosedEvent extends ChatEvent {
    public ChatChannelClosedEvent(ChatService chatService, ChatChannel channel) {
        super(chatService, channel, null, null);
    }
}
