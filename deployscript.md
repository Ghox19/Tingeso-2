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

minikube start

kubectl apply -f postgres-configmap.yaml
kubectl apply -f postgres-secret.yaml
kubectl apply -f postgres.yaml
kubectl apply -f config-server.yaml
kubectl apply -f eureka-server.yaml
kubectl apply -f api-gateway.yaml
kubectl apply -f loan-microservice.yaml

kubectl exec -it postgres-598b5cc7fd-g72ww -- psql -U postgres
CREATE DATABASE loan;
\q



kubectl delete deployment --all
kubectl delete services --all

ps -ef | grep port-forward

kill -9 [PID]

kubectl port-forward service/eureka-server 8761:8761
kubectl port-forward service/api-gateway 8080:8080 
kubectl logs api-gateway-769bfdc8d5-26vbt  

kubectl exec -i postgres_container psql -U postgres -d presta-banco-db < data.sql
kubectl exec -it postgres-598b5cc7fd-fvxt4 -- psql -U postgres -d loan < data.sql

http://localhost:8080/loan-microservice/loan