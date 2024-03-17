#! /bin/bash
cd ../
mvn clean install
docker build -t fileshare .