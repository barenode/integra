# Integra

### Build native image 
```
mvn -Pnative spring-boot:build-image
```

### Run the image
```
docker run -P 7777:8080 hylmar/integra:latest
```

### Inspect required resources for native image
```
java -Dspring.aot.enabled=true -agentlib:native-image-agent=config-output-dir=/path/to/config-dir/ -jar target/integra-0.0.1-SNAPSHOT.jar
```


### Inspect required resources for native image
```
docker build -f Dockerfile_native_agent_inspection -t native-inspector . 

```