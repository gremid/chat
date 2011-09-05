package net.middell.chat;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import net.middell.chat.event.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.newHashSet;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
@Service
public class ChatService implements ApplicationContextAware {
    /**
     * Maximal number of messages queued per channel.
     */
    private static final int MAX_QUEUE_SIZE = 1000;

    /**
     * We remove orphaned logins after 5 minutes of inactivity.
     */
    private static final int LOGIN_EXPIRY = 5 * 60 * 1000;

    private final Map<String, ChatUser> users = Maps.newHashMap();
    private final SortedSetMultimap<ChatChannel, ChatUser> members = TreeMultimap.create();
    private final Map<ChatChannel, Deque<ChatMessage>> messageQueues = Maps.newHashMap();

    private ApplicationContext applicationContext;

    public synchronized ChatUser login(String nickname) {
        final ChatUser user = new ChatUser(nickname);
        Preconditions.checkState(!users.values().contains(user));
        users.put(user.getId(), user);
        return user;
    }

    public synchronized boolean logout(ChatUser user) {
        for (ChatChannel channel : members.keySet()) {
            leave(channel, user);
        }
        return (users.remove(user.getId()) != null);
    }

    public synchronized boolean join(ChatChannel channel, ChatUser member) {
        final boolean joined = members.put(channel, member);
        if (joined) {
            channel.userJoined();
            if (!messageQueues.containsKey(channel)) {
                messageQueues.put(channel, new ArrayDeque<ChatMessage>());
            }
            if (channel.getMembers() == 1) {
                applicationContext.publishEvent(new ChatChannelOpenedEvent(this, channel, member));
            }
            applicationContext.publishEvent(new ChatUserJoinedChannel(this, channel, member));
        }

        return joined;
    }

    public synchronized boolean leave(ChatChannel channel, ChatUser member) {
        final boolean left = members.remove(channel, member);
        if (left) {
            channel.userLeft();
            applicationContext.publishEvent(new ChatUserLeftChannelEvent(this, channel, member));
            if (members.get(channel).isEmpty()) {
                messageQueues.remove(channel);
                applicationContext.publishEvent(new ChatChannelClosedEvent(this, channel));
            }
        }
        return left;
    }

    public synchronized ChatMessage send(ChatChannel channel, ChatUser sender, String message) {
        Preconditions.checkArgument(messageQueues.containsKey(channel));
        Preconditions.checkArgument(members.get(channel).contains(sender));

        final Deque<ChatMessage> channelQueue = messageQueues.get(channel);
        if (channelQueue.size() == MAX_QUEUE_SIZE) {
            channelQueue.removeLast();
        }

        final ChatMessage chatMessage = new ChatMessage(sender, message);
        sender.setLastMessage(chatMessage.getTimestamp());

        channelQueue.push(chatMessage);

        applicationContext.publishEvent(new ChatMessageSentEvent(this, channel, sender, chatMessage));

        return chatMessage;
    }

    public synchronized List<ChatMessage> poll(ChatChannel channel, long last, int max) {
        Preconditions.checkArgument(messageQueues.containsKey(channel));
        final Iterator<ChatMessage> allMessages = messageQueues.get(channel).iterator();

        final LinkedList<ChatMessage> messageWindow = Lists.newLinkedList();
        while (messageWindow.size() < max && allMessages.hasNext()) {
            final ChatMessage message = allMessages.next();
            if (message.getTimestamp() <= last) {
                break;
            }
            messageWindow.addFirst(message);
        }
        return messageWindow;
    }

    public Map<String, ChatUser> getUsers() {
        return Collections.unmodifiableMap(users);
    }

    public SortedSet<ChatChannel> getChannels() {
        return Collections.unmodifiableSortedSet(Sets.newTreeSet(members.keySet()));
    }

    public SortedSetMultimap<ChatChannel, ChatUser> getMembers() {
        return Multimaps.unmodifiableSortedSetMultimap(members);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Scheduled(fixedRate = ChatService.LOGIN_EXPIRY)
    public synchronized void purgeInactiveLogins() {
        final Set<ChatUser> inactive = newHashSet(difference(newHashSet(users.values()), newHashSet(members.values())));
        final long threshold = (System.currentTimeMillis() - LOGIN_EXPIRY);
        for (Iterator<ChatUser> it = inactive.iterator(); it.hasNext(); ) {
            if (it.next().getLastMessage() >= threshold) {
                it.remove();
            }
        }
        for (ChatUser user : inactive) {
            logout(user);
        }
    }

}
