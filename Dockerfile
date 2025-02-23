
FROM gradle:8.7.0-jdk17
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle wasmJsBrowserDistribution --no-daemon
