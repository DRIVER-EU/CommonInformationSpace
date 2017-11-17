# Running the Common Information Space Development Environment on Docker on a local machine

Clone the DRIVER-EU/CommonInformationSpace repository to your machine.

Make sure you have Docker and Docker-Compose installed:
 * On Windows: https://docs.docker.com/docker-for-windows/
 * On Mac: https://docs.docker.com/docker-for-mac/
 * On Linux: https://docs.docker.com/engine/installation/#server and https://docs.docker.com/compose/install/

Run from the repository root directory:
`docker-compose -f cis-beta.yml up -d`

Check whether all containers are running:
`docker ps -a`

To stop the existing CIS development environment:

`docker-compose -f cis-beta.yml stop`

To start the existing CIS development environment:

`docker-compose -f cis-beta.yml start`

To shut down and remove the CIS development environment:
`docker-compose -f cis-beta.yml down`

# Available Services

## Zookeeper

The Zookeeper instance is running and available at localhost:32181

## Kafka Broker

A single Kafka Broker is running and available at PLAINTEXT://localhost:9092

## Schema Registry

The [Kafka Schema Registry](https://docs.confluent.io/current/schema-registry/docs/index.html) is reachable at http://localhost:8081

## Kafka Connect

The [Kafka Connect](https://docs.confluent.io/current/connect/index.html) REST endpoint can be reached at http://localhost:8083

## Kafka REST Proxy

The [Kafka REST Proxy](https://docs.confluent.io/current/kafka-rest/docs/index.html) can be reached at http://localhost:8082

## Kafka Topics UI

A web interface that allows you to browse and search the topics that are available on the Kafka cluster is available at http://localhost:8000

## Kafka Schema Registry UI

A web interface that allows inspection and creation of AVRO schemas in the schema regisrty is available at http://localhost:8001

## Kafka Connect UI

A web interface that allows inspection and creation of Kafka Connectors (data sinks and sources) is available at http://localhost:8002
