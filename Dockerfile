FROM ubuntu:16.04

RUN apt-get update
RUN apt-get install calibre openjdk-8-jdk -y

ADD . /code