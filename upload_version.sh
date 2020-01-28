#!/bin/bash

apiKey=$(cat apikey)
./gradlew clean embeddedgallery:build bintrayUpload -PbintrayUser=goda87 -PbintrayKey=$apiKey -PdryRun=false
