FROM maven:3.8.3-openjdk-17

WORKDIR /app

COPY . /app

RUN mvn clean package

EXPOSE 80

CMD ["mvn", "exec:java"]
