package com.backbase.mockchannel;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageLog {

    private String tag;
    private String trackingId;
    private String channel;
    private int priority;
    private Instant dateSent;
    private Instant dateDelivered;
    private Map<String, Object> payload;
}
