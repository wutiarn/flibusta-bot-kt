FROM ubuntu:16.04

ADD . /code
WORKDIR /code

RUN ./gradlew build
RUN cp build/dist/flibusta-bot.jar .

CMD java -jar flibusta-bot.jar