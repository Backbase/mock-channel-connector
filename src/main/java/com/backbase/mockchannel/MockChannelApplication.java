package com.backbase.mockchannel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
    com.backbase.buildingblocks.commns.service.MessageSenderService.class
})
public class MockChannelApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MockChannelApplication.class, args);
    }
}
