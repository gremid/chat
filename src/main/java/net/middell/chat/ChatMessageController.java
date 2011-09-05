package net.middell.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
@Controller
@RequestMapping("/message")
public class ChatMessageController {

    @Autowired
    private ChatService chatService;

    @RequestMapping("/{channel}")
    @ResponseBody
    public List<ChatMessage> poll(@PathVariable("channel") ChatChannel channel,//
                                  @RequestParam(value = "l", required = false, defaultValue = "0") long last,//
                                  @RequestParam(value = "m", required = false, defaultValue = "10") int max) {
        return chatService.poll(channel, last, max);
    }

    @RequestMapping(value = "/{channel}/{sender}", method = RequestMethod.POST)
    @ResponseBody
    public ChatMessage send(@PathVariable("channel") ChatChannel channel, @PathVariable("sender") ChatUser sender, @RequestBody String message) {
        return chatService.send(channel, sender, message);
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
