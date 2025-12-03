# --- ETAPA 1: BUILD (Compilação) ---
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copia o pom.xml e baixa as dependências (Cache Layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte
COPY src ./src

# Compila o projeto e gera o .jar (Pula testes pra ser mais rápido no deploy)
RUN mvn clean package -DskipTests

# --- ETAPA 2: RUN (Execução) ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia apenas o .jar gerado na etapa anterior
# O nome do jar pode variar, o curinga *.jar resolve isso
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para rodar
ENTRYPOINT ["java", "-jar", "app.jar"]