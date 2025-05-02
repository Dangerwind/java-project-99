FROM eclipse-temurin:21-jdk

ARG GRADLE_VERSION=8.13

# Установка unzip и wget
RUN apt-get update && apt-get install -yq unzip wget

# Установка Gradle
RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip \
    && mv gradle-${GRADLE_VERSION} /opt/gradle

ENV GRADLE_HOME=/opt/gradle
ENV PATH=$PATH:$GRADLE_HOME/bin

# Копируем проект
WORKDIR /app
COPY . .

# Сборка проекта
RUN gradle build --no-daemon

# Предполагаем, что .jar лежит в build/libs
# Указываем порт, который Render ожидает
ENV PORT=8080
EXPOSE 8080

# Запуск .jar (замени имя на фактическое, если отличается)
CMD ["java", "-jar", "build/libs/app-0.0.2-SNAPSHOT.jar"]