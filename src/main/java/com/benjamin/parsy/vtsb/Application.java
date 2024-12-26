package com.benjamin.parsy.vtsb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {

    private final Environment environment;

    public Application(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {

        try {

            InetAddress inetAddress = InetAddress.getLocalHost();
            String host = inetAddress.getHostAddress();

            String serverPort = environment.getRequiredProperty("server.port");
            String virtualThreadsActivate = environment.getRequiredProperty("spring.threads.virtual.enabled");

            log.info("Host : http://{}:{}", host, serverPort);
            log.info("Virtual threads activate : {}", virtualThreadsActivate);

        } catch (UnknownHostException e) {
            log.error("An error occurred while retrieving urls", e);
        }

    }

}
