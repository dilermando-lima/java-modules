FROM alpine
COPY src/ src-docker/

RUN apk add --no-cache openjdk11 && \
    JAVA_HOME=/usr/lib/jvm/java-11-openjdk/bin && \
    $JAVA_HOME/javac -d /built-docker  --module-source-path /src-docker --module example.readfile && \
    mkdir /built-docker/example.readfile/resource/   && \
    cp -r /src-docker/example.readfile/resource/* /built-docker/example.readfile/resource/  && \
    rm -rf jre-custom-docker  && \
    $JAVA_HOME/jlink --module-path $JAVA_HOME/jmods:/built-docker \
    --add-modules example.readfile,java.base  \
     --no-header-files --no-man-pages --compress 2 --strip-debug  \
    --output jre-custom-docker  \
    --launcher example-readfile=example.readfile/example.readfile.run.ReadFile && \
    rm -r /usr/lib/jvm/*

CMD ["sleep","3600000"]
# CMD ["jre-custom-docker/bin/example-readfile"]
