package net.middell.chat;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
public class ChatChannel implements Comparable<ChatChannel> {
    private final String name;
    private int members;

    public ChatChannel(String name) {
        this.name = name;
    }

    @JsonProperty("ch")
    public String getName() {
        return name;
    }

    @JsonProperty("m")
    public int getMembers() {
        return members;
    }

    public void userJoined() {
        members++;
    }

    public void userLeft() {
        members--;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ChatChannel) {
            return name.equals(((ChatChannel) obj).name);
        }
        return super.equals(obj);
    }

    @Override
    public int compareTo(ChatChannel o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(name).toString();
    }

    public static class ToStringConverter implements Converter<ChatChannel, String> {

        @Override
        public String convert(ChatChannel source) {
            return source.getName();
        }
    }

    public static class FromStringConverter implements Converter<String, ChatChannel> {
        private final ChatService chatService;

        public FromStringConverter(ChatService chatService) {
            this.chatService = chatService;
        }

        @Override
        public ChatChannel convert(String source) {
            final ChatChannel key = new ChatChannel(source);
            final ArrayList<ChatChannel> channelList = Lists.newArrayList(chatService.getChannels());

            final int index = Collections.binarySearch(channelList, key);
            return (index < 0 ? key : channelList.get(index));
        }
    }
}
