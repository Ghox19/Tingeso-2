cd Backend

cd backend-config 
mvn clean install 
docker build -t ghox19/backend-config:latest --push . 
cd ..

cd backend-eureka 
mvn clean install 
docker build -t ghox19/backend-eureka --push . 
cd ..

cd backend-gateway 
mvn clean install 
docker build -t ghox19/backend-gateway --push . 
cd ..

cd loan-microservice 
mvn clean install 
docker build -t ghox19/loan-microservice:latest --push .
cd ..

cd client-microservice 
mvn clean install 
docker build -t ghox19/client-microservice:latest --push .
cd ..

cd document-microservice 
mvn clean install 
docker build -t ghox19/document-microservice:latest --push .
cd ..

cd clientloan-microservice 
mvn clean install 
docker build -t ghox19/clientloan-microservice:latest --push .
cd ..

cd tracing-microservice 
mvn clean install 
docker build -t ghox19/tracing-microservice:latest --push .
cd ..

cd saving-microservice 
mvn clean install 
docker build -t ghox19/saving-microservice:latest --push .
cd ..

cd simulation-microservice 
mvn clean install 
docker build -t ghox19/simulation-microservice:latest --push .
cd ..

docker build -t ghox19/react-frontend:latest --push .

minikube start

kubectl apply -f postgres-configmap.yaml
kubectl apply -f postgres-secret.yaml
kubectl apply -f postgres.yaml
kubectl apply -f config-server.yaml
kubectl apply -f eureka-server.yaml
kubectl apply -f api-gateway.yaml
kubectl apply -f loan-microservice.yaml
kubectl apply -f client-microservice.yaml
kubectl apply -f document-microservice.yaml
kubectl apply -f clientloan-microservice.yaml
kubectl apply -f tracing-microservice.yaml
kubectl apply -f saving-microservice.yaml
kubectl apply -f simulation-microservice.yaml
kubectl apply -f react-frontend.yaml

minikube tunnel

minikube service react-frontend

kubectl exec -it postgres-768857d6bf-5b7gh -- psql -U postgres
CREATE DATABASE loan;
DROP DATABASE IF EXISTS nombre_base_datos;
\q

\l

kubectl delete deployment --all
kubectl delete services --all

ps -ef | grep port-forward

kill -9 [PID]

kubectl port-forward service/eureka-server 8761:8761
kubectl port-forward service/api-gateway 8080:8080 
kubectl logs api-gateway-769bfdc8d5-428bg 

kubectl exec -i postgres-768857d6bf-5b7gh -U postgres -d loan < data.sql
kubectl exec -it postgres-768857d6bf-5b7gh -- psql -U postgres -d loan < data.sql

http://localhost:8080/loan-microservice/loan

docker login -u ghox19 -p $dhpsw


kubectl cp data.sql postgres-768857d6bf-5b7gh:/tmp/data.sql

kubectl exec -it postgres-768857d6bf-5b7gh -- psql -U postgres -d loan -f /tmp/data.sql

kubectl run test-pod --image=debian:stable -it --rm -- bash
curl http://api-gateway:8080
