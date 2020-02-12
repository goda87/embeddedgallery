#!/bin/bash

apiKey=$(cat secret/apikey)
./gradlew clean embeddedgallery:build bintrayUpload -PbintrayUser=goda87 -PbintrayKey=$apiKey -PdryRun=false
