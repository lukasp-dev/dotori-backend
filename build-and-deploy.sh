#!/bin/bash

echo "Packaging Spring Boot app..."
./mvnw package -DskipTests

echo "Copying JAR to docker directory..."
cp target/backend-0.0.1-SNAPSHOT.jar docker/backend.jar

echo "Deploying to Cloud Build..."
gcloud builds submit docker \
  --tag gcr.io/dotori-456603/dotori-backend

echo "Deploying to Cloud Run..."
gcloud run deploy dotori-backend \
  --image gcr.io/dotori-456603/dotori-backend \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated