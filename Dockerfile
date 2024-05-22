FROM openjdk:19-alpine

ENV DATABASE_URL=$DATABASE_URL
ENV DATABASE_USER=$DATABASE_USER
ENV DATABASE_DRIVER=$DATABASE_DRIVER
ENV DATABASE_PASSWORD=$DATABASE_PASSWORD

RUN mkdir /app
WORKDIR /app
COPY ./build/libs/com.trash.trashtrade-all-0.0.1.jar .
CMD java -jar com.trash.trashtrade-all-0.0.1.jar