# RSong Acquisition
A minimal utility for loading RSong binary assets to RChain.

## Caution
This project is still under active development. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system

## Install

- Prerequisites
- Clone & Build
- Execute 

### Prerequisites

- [sbt](https://www.scala-sbt.org/)
- [JDK8](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html?printOnly=1)
- [docker](https://www.docker.com/) <Optional>


#### Clone & Build 

```
git clone git@github.com:kayvank/rsong-acquisition.git
cd rsong-acquisition
sbt compile 
sbt universal:packageBin
```

## Execute

To run the project locally:
- make sure you have a local instance of rnode up and running
- execute the project:

```
sbt run
## alternatively
sbt clean compile universal:pacageBin
cd target/universal
unzip ./rsong-acquisition-1.2-SNAPSHOT.zip
cd rsong-acquisition-1.2-SNAPSHOT/

./bin/rsong-acquisition   

```
### Detail
- Install rsong contract:

```$xslt
./bin/rsong-acquisition  Install  

```
- Deploy and Propose rsong assets to block-chain:

```
./bin/rsong-acquisition  Deploy

```

- Install contract, deploy and propose assets

```$xslt
./bin/rsong-acquisition 

```


### Environment variables

tbd

### Running tests:
There are minimal unit tests.

tests are:
- unit tests
- integration tests

#### Unit tests
```
sbt clean test
```
## Future work
- rsong-acquisition should work against an asset manifest
- add acquisitions against aws s3, dropbox, and other cloud related storage dev.
- use environment variables to target diff rnode installations
- impl monix

## References

- [docker-image](https://hub.docker.com/r/kayvank/immersion-rc-proxy/tags/)
- [Rholang](https://developer.rchain.coop/assets/rholang-spec-0.2.pdf)
- [RChain Cooperative](https://www.rchain.coop/)
- [http4s](https://github.com/http4s/http4s)



