# JWeb Console

![master status](https://github.com/pintowar/jweb-console/actions/workflows/master.yml/badge.svg?branch=master)
[![Sonar Coverage](https://sonarcloud.io/api/project_badges/measure?project=pintowar_jweb-console&metric=coverage)](https://sonarcloud.io/dashboard?id=pintowar_jweb-console)

![develop status](https://github.com/pintowar/jweb-console/actions/workflows/develop.yml/badge.svg?branch=develop)

![GitHub tag (latest)](https://img.shields.io/github/v/tag/pintowar/jweb-console)
![GitHub license](https://img.shields.io/github/license/pintowar/jweb-console)

Overview
--------
This application is intended for embedding a web console in a java web application (Spring Boot)
which then could access the application context (for development purposes).

Usage
-----

Add framework and lang dependencies:

```kotlin
developmentOnly("io.github.pintowar:jweb-console-spring-boot-starter:x.y.z")
developmentOnly("io.github.pintowar:jweb-console-groovy:x.y.z")
```

Add the `jweb.console.enabled=true` config to the application config (preferably only on dev environment).

After the application is started, the console can be accessed on the url: `http://localhost:8080/console`

A sample application can be found on the `samples` folder.