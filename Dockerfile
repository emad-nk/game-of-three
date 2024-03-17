FROM maven:3.8.5-openjdk-17

RUN mkdir -p /app
WORKDIR /app

ADD . /app
