package com.backbase.mockchannel;

import com.backbase.buildingblocks.commns.listener.CommnsAbstractMessagesStreamListener;
import com.backbase.buildingblocks.commns.service.AbstractPriorityQueueService;
import com.backbase.buildingblocks.commns.service.TrackingService;
import com.backbase.buildingblocks.multitenancy.TenantProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MockMessageListener extends CommnsAbstractMessagesStreamListener<Map<String,Object>> {

    public MockMessageListener(
        AbstractPriorityQueueService<Map<String,Object>> priorityQueueService,
        TrackingService trackingService,
        TenantProvider tenantProvider) {
        super(priorityQueueService, trackingService, new ObjectMapper(), tenantProvider);
    }
}
