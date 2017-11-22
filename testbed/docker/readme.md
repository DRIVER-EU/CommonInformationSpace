# Testbed Docker Compose Files

In this directory you find the Testbed Docker Compose files that allow you to run the testbed on any machine that is [configured as a Docker host](https://docs.docker.com/engine/installation/).

## Initializing a new Local Test Bed

1. Navigate to the 'CommonInformationSpace/testbed/docker' directory
2. Run from the console: `docker-compose -f testbed-local.yml up -d`
3. Check if all the containers are running: `docker ps -a`

## Starting/Stopping the Local Test Bed

* To stop, run from the console: `docker-compose -f testbed-local.yml stop`
* To start, run from the console: `docker-compose -f testbed-local.yml start`

##  Stop and Remove the Local Test Bed

* To stop and remove, run from the console: `docker-compose -f testbed-local.yml down`

This command removes all containers and thus data and configuration that was present in the testbed.

# Configuring your Hosts File

If order to allow using the services provided by the testbed, several entries need to be [added to your hosts file](https://www.howtogeek.com/howto/27350/beginner-geek-how-to-edit-your-hosts-file/).

## Hosts Entries for Local Test Bed

```
127.0.0.1	    broker
127.0.0.1	    zookeeper
127.0.0.1     kafka_rest
127.0.0.1	    schema_registry
127.0.0.1	    connect
```
## Hosts Entries for the Testbed at TNO

```
134.221.20.240	    broker
134.221.20.240	    zookeeper
134.221.20.240     kafka_rest
134.221.20.240	    schema_registry
134.221.20.240	    connect
```

# Available Testbed Services

Note: in order to use these services make sure you have added the above entries to your hosts file!

## Zookeeper

The Zookeeper instance is running and available at `zookeeper:3500`

## Kafka Broker

A single Kafka Broker is running and available at `PLAINTEXT://broker:9092`

## Schema Registry

The [Kafka Schema Registry](https://docs.confluent.io/current/schema-registry/docs/index.html) is reachable at http://schema_registry:3502 or http://localhost:3502

## Kafka Connect

The [Kafka Connect](https://docs.confluent.io/current/connect/index.html) REST endpoint is reachable at http://connect:3504 or http://localhost:3504.

## Kafka REST Proxy

The [Kafka REST Proxy](https://docs.confluent.io/current/kafka-rest/docs/index.html) can be reached at http://kafka_rest:8082.

## Kafka Topics UI

A web interface that allows you to browse and search the topics that are available on the Kafka cluster is available at http://localhost:3600 if you run locally, or http://driver-testbed.eu:3600 if you use the TNO testbed.

## Kafka Schema Registry UI

A web interface that allows inspection and creation of AVRO schemas in the schema regisrty is available at http://localhost:3601 if you run locally, or http://driver-testbed.eu:3601 if you use the TNO testbed.

## Kafka Connect UI

A web interface that allows inspection and creation of Kafka Connectors (data sinks and sources) is available at http://localhost:3602 if you run locally, or http://driver-testbed.eu:3602 if you use the TNO testbed.
