FROM ubuntu:16.04

ADD . /code
WORKDIR /code

RUN ./gradlew shadowJar
RUN cp build/dist/flibusta-bot.jar .

CMD java -jar flibusta-bot.jar