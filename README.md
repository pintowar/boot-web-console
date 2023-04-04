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

How to compile
--------------

    mvnw clean package

Usage
-----
Java 8 or later is required to start the application.

* Start server: `mvnw spring-boot:run` or `java -jar target/groovy-web-console-1.0.0-SNAPSHOT.jar`
* Access the console: `http://localhost:8080/console`
