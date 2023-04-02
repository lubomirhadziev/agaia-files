FROM bellsoft/liberica-runtime-container:jdk-17-stream-musl
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
CMD java -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20 -XX:MinMetaspaceFreeRatio=10 -XX:MaxMetaspaceFreeRatio=20 -jar /app.jar