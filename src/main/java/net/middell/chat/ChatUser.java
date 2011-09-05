package net.middell.chat;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
public class ChatUser implements Comparable<ChatUser> {
    private final String nickName;
    private final String id;
    private long lastMessage;

    public ChatUser(String nickName) {
        this.nickName = nickName;
        this.id = UUID.randomUUID().toString();
    }

    @JsonProperty("n")
    public String getNickName() {
        return nickName;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonIgnore
    public long getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(long lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int hashCode() {
        return nickName.hashCode();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("nick", nickName).add("id", id).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ChatUser) {
            return nickName.equals(((ChatUser) obj).nickName);
        }
        return super.equals(obj);
    }

    @Override
    public int compareTo(ChatUser o) {
        return nickName.compareTo(o.nickName);
    }

    public static class ToStringConverter implements Converter<ChatUser, String> {

        @Override
        public String convert(ChatUser source) {
            return source.getNickName();
        }
    }

    public static class FromStringConverter implements Converter<String, ChatUser> {
        private final ChatService chatService;

        public FromStringConverter(ChatService chatService) {
            this.chatService = chatService;
        }

        @Override
        public ChatUser convert(String source) {
            final Map<String,ChatUser> users = chatService.getUsers();
            Preconditions.checkState(users.containsKey(source));
            return users.get(source);
        }
    }
}
