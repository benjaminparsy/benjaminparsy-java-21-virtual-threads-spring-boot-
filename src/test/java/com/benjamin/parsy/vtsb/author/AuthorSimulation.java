package com.benjamin.parsy.vtsb.author;

import com.benjamin.parsy.vtsb.shared.web.HttpHeaderName;
import com.github.javafaker.Faker;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.springframework.http.MediaType;

import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

/**
 * This class is used to configure the gatling performance test
 */
public class AuthorSimulation extends Simulation {

    private static final HttpProtocolBuilder HTTP_PROTOCOL_BUILDER = setupProtocolForSimulation();
    private static final Iterator<Map<String, Object>> FEED_DATA = setupTestFeedData();
    private static final ScenarioBuilder POST_SCENARIO_BUILDER = buildPostScenario();

    public AuthorSimulation() {

        setUp(POST_SCENARIO_BUILDER
                .injectOpen(userInjection())
                .protocols(HTTP_PROTOCOL_BUILDER));
    }

    /**
     * Http parameter configuration
     */
    private static HttpProtocolBuilder setupProtocolForSimulation() {

        return HttpDsl.http.baseUrl("http://localhost:8080")
                .acceptHeader(MediaType.APPLICATION_JSON_VALUE)
                .maxConnectionsPerHost(10)
                .userAgentHeader("Gatling/Performance Test");
    }

    /**
     * Feeds queries programmed in the scenario
     */
    private static Iterator<Map<String, Object>> setupTestFeedData() {

        Faker faker = new Faker();
        Iterator<Map<String, Object>> iterator;
        iterator = Stream.generate(() -> {

                    Map<String, Object> stringObjectMap = new HashMap<>();
                    stringObjectMap.put("firstname", faker.name().firstName());
                    stringObjectMap.put("lastname", faker.name().lastName());

                    return stringObjectMap;
                })
                .iterator();

        return iterator;
    }

    /**
     * Test scenario to be performed
     */
    private static ScenarioBuilder buildPostScenario() {

        // Body json preparation
        String bodyJson = """
                {
                    "firstname": "${firstname}",
                    "lastname": "${lastname}"
                }
                """;

        return CoreDsl.scenario("Create and get authors")
                .feed(FEED_DATA)
                .exec(http("create-authors")
                        .post("/authors")
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaderName.USER_ID.getName(), session -> String.valueOf(session.userId()))
                        .body(StringBody(bodyJson))
                        .check(status().is(201))
                );
    }

    /**
     * Manage number of users / request
     */
    private OpenInjectionStep userInjection() {

        String requestsPerSecondProperty = System.getenv().get("GATLING_REQUESTS_PER_SECOND");
        String durationInSecondesProperty = System.getenv().get("GATLING_DURATION_SECONDS");

        System.out.println("==============================================================");
        System.out.println("Requests per second : " + requestsPerSecondProperty);
        System.out.println("Duration in seconds : " + durationInSecondesProperty);
        System.out.println("==============================================================");

        int requestsPerSecond = Integer.parseInt(requestsPerSecondProperty);
        Duration durationInSecondes = Duration.ofSeconds(Long.parseLong(durationInSecondesProperty));

        return CoreDsl.constantUsersPerSec(requestsPerSecond) // Number of request per second
                .during(durationInSecondes); // Time of the test
    }

}
