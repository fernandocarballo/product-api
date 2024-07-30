# product-api

[![Build](https://github.com/fernandocarballo/product-api/actions/workflows/build.yml/badge.svg)](https://github.com/fernandocarballo/product-api/actions/workflows/build.yml)
[![Test cases](https://github.com/fernandocarballo/product-api/actions/workflows/test.yml/badge.svg)](https://github.com/fernandocarballo/product-api/actions/workflows/test.yml)
[![Delivery Docker Image](https://github.com/fernandocarballo/product-api/actions/workflows/delivery.yml/badge.svg)](https://github.com/fernandocarballo/product-api/actions/workflows/delivery.yml)

Api Rest desarrollada en Java para la creacion, modificacion, eliminacion y consulta de productos.
Esta api esta desarrollada con estandares de OpenApi, y documentada con Swagger.

## Metodos disponibles
- Insert
- Update
- Delete
- Get
- FindByName
- List

## Requerimientos
- Java 21

## Uso
Descargue este proyecto y ejecutelo. Por defecto el servicio esta disponible en el puerto 8070.
Por ejemplo: http://localhost:8070/

### Seguridad
El acceso a la API se encuentra segurizado mediante Basic Auth.
- `Usuario` admin
- `Contraseña` 1234

### Documentacion y UI
El servicio despliega una web para poder usar la api y comprender el uso de las funcionalidades. 
Disponible en: [http://localhost:8070/shagger-ui/index.html](http://localhost:8070/swagger-ui/index.html)
La misma no esta segurizada para que pueda ser consultada sin login.

## Imagen Docker
Puede disponer de esta imagen actualizada en el repositorio de Docker Hub: https://hub.docker.com/repository/docker/fernandocarballo/product-api

### Despliegue
El siguiente comando le permite usar la api desde un repositorio docker. Habilitando el acceso en el puerto 8070
```
docker run -p 8070:8070 fernandocarballo/product-api
```
