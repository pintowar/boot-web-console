# Spring Boot Web Console

![master status](https://github.com/pintowar/boot-web-console/actions/workflows/master.yml/badge.svg?branch=master)
[![Sonar Coverage](https://sonarcloud.io/api/project_badges/measure?project=pintowar_boot-web-console&metric=coverage)](https://sonarcloud.io/dashboard?id=pintowar_boot-web-console)

![develop status](https://github.com/pintowar/boot-web-console/actions/workflows/develop.yml/badge.svg?branch=develop)

![GitHub tag (latest)](https://img.shields.io/github/v/tag/pintowar/boot-web-console)
![GitHub license](https://img.shields.io/github/license/pintowar/boot-web-console)

Overview
--------
This application is intended for embedding a Groovy console in a Spring Boot web application
which then could access the application context (for testing purposes).

Usage
-----

Add core dependency:

```kotlin
implementation("io.github.pintowar:boot-web-console-core:x.y.z")
```

Add `io.github.pintowar.console` package to the `scanBasePackages` property of the `@SpringBootApplication` annotation like:

```java
@SpringBootApplication(scanBasePackages = {"io.github.pintowar.console"})
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

After the application is started, the console can be accessed on the url: `http://localhost:8080/console`

A sample application can be found on the `boot-web-console-sample`.