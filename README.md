## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Requirements](#requirements)
- [Local](#local)
- [Coding standards](#coding-standards)
- [Testing](#testing)
- [API](#api)

## Introduction

The project is a simple Spring Boot service, that is able to calculate any possible land
route from one country to another. The country data is fetched from an external source

## Features

The project contains one REST API endpoint for providing a list of border crossings to get from origin to destination.

## Requirements

* [Java 17 SDK](https://www.oracle.com/java/technologies/downloads/#java17)
* [Maven](https://maven.apache.org/download.cgi)

## Local

```bash
$ mvn spring-boot:run
```

Application will run by default on port `8081`

Configure the port by changing `server.port` in __application.yml__

## Coding standards

- The code was formatted using IntelliJ IDEA's "google-java-format" plugin. Consider using the same plugin to keep the
  format of the code consistent.

## Testing

The project provides both unit and integration tests.

For manual testing purposes one can use a few example calls provided in "/test/resources/routeController.http".

## API

The API takes country codes of origin and destination and returns a result containing a single possible route
represented by a list of border crossings

**Find route**

```http
GET /api/v1/routing/{origin}/{destination}
```

For example calls please check "/test/resources/routeController.http".

**Response format**

```code
{
    "route" : array[string],
}
```
