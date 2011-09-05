package net.middell.chat.event;

import net.middell.chat.ChatChannel;
import net.middell.chat.ChatMessage;
import net.middell.chat.ChatUser;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
public class ChatMessageSentEvent extends ChatEvent {
    public ChatMessageSentEvent(Object source, ChatChannel channel, ChatUser sender, ChatMessage message) {
        super(source, channel, sender, message);
    }
}
