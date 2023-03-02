#!/usr/bin/env bash
mvn package
docker build -f relay/src/main/docker/Dockerfile.jvm -t com.nipsr/relay:latest-jvm relay
docker build -f processor/src/main/docker/Dockerfile.jvm -t com.nipsr/processor:latest-jvm processor