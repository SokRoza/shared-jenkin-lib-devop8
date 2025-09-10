def call(Map config = [:]) {
    pipeline {
        agent any

        environment {
            IMAGE_NAME = config.imageName ?: "react-app"
            REGISTRY   = config.registry ?: "dockerhub_username"
            PORT       = config.port ?: 80
        }

        stages {
            stage('Inject Dockerfile') {
                steps {
                    script {
                        if (!fileExists('Dockerfile')) {
                            echo "Dockerfile not found. Generating..."
                            writeFile file: 'Dockerfile', text: """
FROM node:alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/build /usr/share/nginx/html
EXPOSE ${PORT}
CMD ["nginx", "-g", "daemon off;"]
"""
                        } else {
                            echo "Dockerfile exists. Using existing."
                        }
                    }
                }
            }

            stage('Inject docker-compose.yml') {
                steps {
                    script {
                        if (!fileExists('docker-compose.yml')) {
                            echo "docker-compose.yml not found. Generating..."
                            writeFile file: 'docker-compose.yml', text: """
version: '3.9'
services:
  reactjs-srv:
    image: ${REGISTRY}/${IMAGE_NAME}:latest
    container_name: ${IMAGE_NAME}-container
    ports:
      - "3000:${PORT}"
"""
                        } else {
                            echo "docker-compose.yml exists. Using existing."
                        }
                    }
                }
            }
        }
    }
}
