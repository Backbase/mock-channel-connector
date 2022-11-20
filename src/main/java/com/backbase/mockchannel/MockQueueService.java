package com.backbase.mockchannel;

import com.backbase.buildingblocks.commns.service.AbstractPriorityQueueService;
import com.backbase.buildingblocks.multitenancy.TenantProvider;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MockQueueService extends AbstractPriorityQueueService<Map<String,Object>> {

    public MockQueueService(@Value("${backbase.mock.workers}") int numberOfWorkers,
        MockConsumer mockConsumer, TenantProvider tenantProvider) {
        super(mockConsumer, numberOfWorkers, tenantProvider);
    }
}
