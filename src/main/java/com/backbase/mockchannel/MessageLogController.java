package com.backbase.mockchannel;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * {@link MessageLogController}
 * <br>
 * {@code com.backbase.mockchannel.MessageLogController}
 * <br>
 *
 * @author Jaco Botha
 * @since 30 August 2022
 */
@Controller
public class MessageLogController {

    @MessageMapping("/message")
    @SendTo("/topic/message-log")
    public MessageLog messageLog(@Payload MessageLog message) throws Exception {
        return message;
    }
}
