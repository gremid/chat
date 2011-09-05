package net.middell.chat;

import net.middell.chat.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
@Service
public class ChatEventLogger implements ApplicationListener<ChatEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(ChatEventLogger.class.getPackage().getName());


    @Override
    public void onApplicationEvent(ChatEvent event) {
        if (!LOG.isTraceEnabled()) {
            return;
        }
        if (event instanceof ChatMessageSentEvent) {
            LOG.trace("{} in {}", event.getMessage(), event.getChannel());
        } else if (event instanceof ChatUserJoinedChannel) {
            LOG.trace("{} joined {}", event.getUser(), event.getChannel());
        } else if (event instanceof ChatUserLeftChannelEvent) {
            LOG.trace("{} left {}", event.getUser(), event.getChannel());
        } else if (event instanceof ChatChannelOpenedEvent) {
            LOG.trace("{} opened by {}", event.getChannel(), event.getUser());
        } else if (event instanceof ChatChannelClosedEvent) {
            LOG.trace("{} closed", event.getChannel());
        }
    }
}
