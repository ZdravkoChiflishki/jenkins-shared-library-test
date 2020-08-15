# syntax=docker/dockerfile:experimental

FROM gradle:6.2.2-jdk11 AS build
COPY . /cop-ci-lib
WORKDIR /cop-ci-lib
RUN --mount=type=cache,target=/root/.gradle gradle build
