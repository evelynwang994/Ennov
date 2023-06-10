FROM openjdk:17

RUN mkdir -p /software/app

ADD product-aggregator.war /software/app/product-aggregator.war

ENV port=8080

CMD java -jar /software/app/product-aggregator.war