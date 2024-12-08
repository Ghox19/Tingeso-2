# Etapa de construcción
FROM --platform=linux/amd64 node:alpine AS build
# Establecer el directorio de trabajo
WORKDIR /app
# Copiar los archivos de configuración de npm
COPY package*.json ./
# Instalar las dependencias
RUN npm install
# Copiar los archivos de la aplicación
COPY . .
# Construir la aplicación
RUN npm run build

# Etapa de producción
FROM --platform=linux/amd64 nginx:latest
# Copiar los archivos construidos al servidor Nginx
COPY --from=build /app/build /usr/share/nginx/html
# Exponer el puerto 80
EXPOSE 80
# Comando para ejecutar Nginx en primer plano
CMD ["nginx", "-g", "daemon off;"]