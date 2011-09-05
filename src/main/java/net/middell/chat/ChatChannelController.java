package net.middell.chat;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.SortedSet;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
@Controller
@RequestMapping("/channel")
public class ChatChannelController {

    @Autowired
    private ChatService chatService;


    @RequestMapping
    @ResponseBody
    public SortedSet<ChatChannel> channels() {
        return chatService.getChannels();
    }


    @RequestMapping("/{channel}")
    @ResponseBody
    public SortedSet<ChatUser> members(@PathVariable("channel") ChatChannel channel) {
        Preconditions.checkArgument(chatService.getChannels().contains(channel));
        return Sets.newTreeSet(chatService.getMembers().get(channel));
    }

    @RequestMapping(value = "/{channel}/{member}", method = RequestMethod.POST)
    @ResponseBody
    public boolean join(@PathVariable("channel") ChatChannel channel, @PathVariable("member") ChatUser member) {
        return chatService.join(channel, member);
    }

    @RequestMapping(value = "/{channel}/{member}")
    public String display(@PathVariable("channel") ChatChannel channel, @PathVariable("member") ChatUser member) {
        return "channel";
    }

    @RequestMapping(value = "/{channel}/{member}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean leave(@PathVariable("channel") ChatChannel channel, @PathVariable("member") ChatUser member) {
        return chatService.leave(channel, member);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFound() {
    }

    @ExceptionHandler({IllegalStateException.class})
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public void preconditionFailed() {
    }
}
