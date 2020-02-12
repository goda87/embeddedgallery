#!/bin/bash

function getProperty() {
    grep $1 secret/privateRepository.properties | cut -d '=' -f2
}

privateGroupoId=`getProperty privateGroupoId`
versionAnnotation=`getProperty versionAnnotation`
echo "./gradlew clean embeddedgallery:build embeddedgallery:publishReleasePublicationToAndroidLibrariesRepository -PgroupId=$privateGroupoId -PversionAnnotation=$versionAnnotation"
./gradlew clean embeddedgallery:build embeddedgallery:publishReleasePublicationToAndroidLibrariesRepository -PgroupId=$privateGroupoId -PversionAnnotation=$versionAnnotation
