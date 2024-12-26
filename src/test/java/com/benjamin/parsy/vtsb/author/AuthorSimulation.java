package com.benjamin.parsy.vtsb.author;

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

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class AuthorSimulation extends Simulation {

    private static final HttpProtocolBuilder HTTP_PROTOCOL_BUILDER = setupProtocolForSimulation();
    private static final Iterator<Map<String, Object>> FEED_DATA = setupTestFeedData();
    private static final ScenarioBuilder POST_SCENARIO_BUILDER = buildPostScenario();

    public AuthorSimulation() {

        int maxResponseTimeInSeconds = (int) Duration.ofSeconds(10).toMillis();
        double minPercentSuccessfulRequests = 90d;

        setUp(POST_SCENARIO_BUILDER.injectOpen(injection())
                .protocols(HTTP_PROTOCOL_BUILDER))
                .assertions(
                        global().responseTime().max().lte(maxResponseTimeInSeconds),
                        global().successfulRequests().percent().gt(minPercentSuccessfulRequests)
                );
    }

    private static HttpProtocolBuilder setupProtocolForSimulation() {

        final String host = System.getenv().getOrDefault("BASE_URL", "http://localhost:8080");

        return HttpDsl.http.baseUrl(host)
                .acceptHeader(MediaType.APPLICATION_JSON_VALUE)
                .maxConnectionsPerHost(10)
                .userAgentHeader("Gatling/Performance Test");
    }

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

    private static ScenarioBuilder buildPostScenario() {

        String bodyJson = """
            {
                "firstname": "${firstname}",
                "lastname": "${lastname}"
            }
            """;

        return CoreDsl.scenario("Load Test Creating Authors")
                .feed(FEED_DATA)
                .exec(http("create-authors").post("/authors")
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .body(StringBody(bodyJson))
                        .check(status().is(201)))
                .exec(http("get-authors").get("/authors")
                        .check(status().is(200)));
    }

    private OpenInjectionStep.RampRate.RampRateOpenInjectionStep injection() {

        String usersAddedPerSecondProperty = System.getenv().get("GATLING_USERS_ADDED_PER_SECOND");
        String totalDesiredUserCountProperty = System.getenv().get("GATLING_TOTAL_USER");
        String steadyStateDurationMinutesProperty = System.getenv().get("GATLING_DURATION_MINUTES");

        System.out.println("==============================================================");
        System.out.println("Users added per second : " + usersAddedPerSecondProperty);
        System.out.println("Total desired user count : " + totalDesiredUserCountProperty);
        System.out.println("Steady state duration minutes : " + steadyStateDurationMinutesProperty);
        System.out.println("==============================================================");

        int usersAddedPerSecond = Integer.parseInt(usersAddedPerSecondProperty);
        int totalDesiredUserCount = Integer.parseInt(totalDesiredUserCountProperty);
        Duration totalRampUptimeSeconds = Duration.ofSeconds(totalDesiredUserCount / usersAddedPerSecond);
        int steadyStateDurationMinutes = Integer.parseInt(steadyStateDurationMinutesProperty);

        return rampUsersPerSec(usersAddedPerSecond)
                .to(totalDesiredUserCount)
                .during(totalRampUptimeSeconds.plusMinutes(steadyStateDurationMinutes));
    }

}
