#!/usr/bin/env sh

./mvnw clean install -DskipTests

unzip target/example-1.0-SNAPSHOT.jar -d target/example

native-image --allow-incomplete-classpath --no-fallback --enable-https \
  -H:+ReportUnsupportedElementsAtRuntime -Dfile.encoding=UTF-8 \
  -cp ".:$(echo target/example/BOOT-INF/lib/*.jar | tr ' ' ':')":target/example/BOOT-INF/classes \
  TestKt
