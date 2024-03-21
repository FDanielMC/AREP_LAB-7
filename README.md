# Laboratorio 7: APLICACIÓN DISTRIBUIDA SEGURA EN TODOS SUS FRENTES

Este proyecto implica la creación de una aplicación web que asegura un acceso seguro desde el navegador. Se enfoca en garantizar la autenticación, autorización y la integridad de los usuarios que interactúan con ella. Todo esto se logra mediante el uso de certificados digitales y la implementación de protocolos de comunicación seguros como HTTPS. La aplicación está compuesta por al menos dos servicios que se comunican entre sí de manera segura. Esta comunicación entre servicios se encarga de verificar la autenticidad, autorización y la integridad de las interacciones. De esta manera, solo los servicios autorizados pueden invocar a otros servicios, evitando cualquier intento de acceso no autorizado a los servicios de la aplicación.

## Primeros Pasos

### PREREQUISITOS

* [Java (desde la 15 para delante)](https://www.oracle.com/co/java/technologies/downloads/) 
* [Maven](https://maven.apache.org/download.cgi) 
* [Git](https://git-scm.com/downloads)

### INSTALACIÓN Y EJECUCIÓN 

1. Clona el repositorio:
```
https://github.com/FDanielMC/AREP_LAB-7.git
```

2. Ve a la carpeta del repositorio clonado:
```
cd AREP_LAB-7
```

3. Usa el siguiente comando para construir el proyecto:
```
mvn clean install
```

4. Para ejecutar la aplicación se debe correr los servicios SecureLogin (38000) y UserManager(35000):
```
java -cp target/AREP-Lab7-1.0-SNAPSHOT.jar edu.eci.arep.SecureLogin
```

```
java -cp target/AREP-Lab7-1.0-SNAPSHOT.jar edu.eci.arep.UserManager
```
