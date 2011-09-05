package net.middell.chat;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * @author <a href="http://gregor.middell.net/" title="Homepage">Gregor Middell</a>
 */
@Controller
@RequestMapping("/user")
public class ChatUserController {

    @Autowired
    private ChatService chatService;

    @RequestMapping(value = "/login/{nickname}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> login(@PathVariable("nickname") String nickname) {
        return Collections.singletonMap("id", chatService.login(nickname).getId());
    }

    @RequestMapping(value = "/logout/{user}", method = RequestMethod.POST)
    @ResponseBody
    public boolean login(@PathVariable("user") ChatUser user) {
        return chatService.logout(user);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public void illegalState() {
    }
}
