package com.backbase.mockchannel;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockConsumer implements Consumer<Message<Map<String,Object>>> {

    private final SimpMessagingTemplate template;

    public void accept(Message<Map<String,Object>> msg) {
        MessageLog messageLog = new MessageLog(
            (String) msg.getHeaders().get("msgTag"),
            (String) msg.getHeaders().get("trackingId"),
            (String) msg.getHeaders().get("msgChannel"),
            msg.getHeaders().get("msgPriority", Integer.class),
            Optional.ofNullable(msg.getHeaders().getTimestamp()).map(Instant::ofEpochMilli).orElse(null),
            Instant.now()
        );
        log.info("Message Received: {}", messageLog);
        this.template.convertAndSend("/topic/message-log", messageLog);
    }
}
