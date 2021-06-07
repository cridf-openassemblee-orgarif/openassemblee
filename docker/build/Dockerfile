FROM openjdk:8-alpine

RUN apk add git nodejs npm yarn maven unzip wget

RUN npm install -g bower grunt-cli

WORKDIR /src

CMD git clone --depth 1 file:///repo && cd repo && wget https://mlorber.net/backup/cridf/node_modules_21-06-07.zip && unzip node_modules_21-06-07.zip && mvn install -X -Pprod -DskipTests -D=mysql.connector.version=5.1.37
