package com.backbase.mockchannel;

import com.backbase.buildingblocks.testutils.TestTokenUtil;
import java.io.File;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * {@link MockConsumerIT}
 * <br>
 * {@code com.backbase.mockchannel.MockConsumerIT}
 * <br>
 *
 * @author Jaco Botha
 * @since 23 November 2022
 */
@Slf4j
@Testcontainers
@RequiredArgsConstructor
@ActiveProfiles({"default", "it"})
@SpringBootTest(classes = {MockChannelApplication.class})
@ContextConfiguration(classes = MockChannelApplication.class, initializers = {MockConsumerIT.Initializer.class})
class MockConsumerIT {

    private final RestTemplate template = new RestTemplate();

    @Container
    public static DockerComposeContainer environment = new DockerComposeContainer(
        new File("src/test/resources/docker-compose.yml"))
        .withExposedService("message-broker", 61616)
        .withExposedService("communication", 8080)
        .withLogConsumer("message-broker", new Slf4jLogConsumer(log))
        .withLogConsumer("communication", new Slf4jLogConsumer(log))
        .withLocalCompose(true);

    @BeforeAll
    public static void envSetup() {
        System.setProperty("SIG_SECRET_KEY", "JWTSecretKeyDontUseInProduction!");
        System.setProperty("TESTCONTAINERS_RYUK_DISABLED", "true");
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            environment.start();

            TestPropertyValues.of(
                    "spring.activemq.broker-url=tcp://%s:%s"
                        .formatted(environment.getServiceHost("message-broker", 61616),
                            environment.getServicePort("message-broker", 61616)))
                .applyTo(configurableApplicationContext.getEnvironment());

        }
    }

    @Test
    void sendAnEmailFormCommunication() throws InterruptedException {
        String host = environment.getServiceHost("communication", 8080);
        int port = environment.getServicePort("communication", 8080);

        String uuid = UUID.randomUUID().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TestTokenUtil.encode(TestTokenUtil.serviceClaimSet()));

        RequestEntity<String> request = RequestEntity.post("http://%s:%s/service-api/v1/messages".formatted(host, port))
            .headers(headers)
            .body("""
                {
                  "messages": [
                    {
                      "deliveryChannel": "email",
                      "priority": 0,
                      "tag": "%s",
                      "payload": {
                        "subject": "Important Email Subject",
                        "body": "This is the email body for john",
                        "to": "john@domain.com"
                      }
                    }
                  ]
                }
                """.formatted(uuid));

        ResponseEntity<Void> exchange = template.exchange(request, Void.TYPE);
        Assertions.assertEquals(HttpStatus.ACCEPTED, exchange.getStatusCode());
    }
}