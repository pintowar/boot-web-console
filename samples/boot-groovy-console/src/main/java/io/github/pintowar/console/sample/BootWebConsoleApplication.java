package io.github.pintowar.console.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"io.github.pintowar.console"})
public class BootWebConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootWebConsoleApplication.class, args);
    }
}
