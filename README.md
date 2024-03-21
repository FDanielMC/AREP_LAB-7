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
git clone https://github.com/FDanielMC/AREP_LAB-7.git
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

5. En un navegador ve al siguiente enlace para probar el programa: https://localhost:38000/login.html

### EJECUTAR PRUEBAS

Para ejecutar las pruebas ingrese el siguiente comando en la línea de comandos:
```
mvn test
```

## ARQUITECTURA

La aplicación web está compuesta por dos servicios: SecureLogin y UserManager. 
* **SecureLogin:** Este servicio desempeña un papel fundamental como el punto de contacto inicial para la autenticación de usuarios. Su función principal es gestionar las solicitudes de inicio de sesión provenientes del navegador web, llevando a cabo la validación de las credenciales de manera segura. Para garantizar una comunicación protegida y cifrada con el cliente, este servicio utiliza el protocolo HTTPS.
* **UserManager:**  Este servicio tiene la responsabilidad de administrar de manera segura a los usuarios y sus contraseñas almacenadas mediante hashes. Además, ofrece una API para la verificación de las contraseñas de los usuarios. Todo esto se lleva a cabo con una comunicación segura a través del protocolo HTTPS.

Ambos servicios se apoyan en certificados digitales autofirmados, almacenados en un almacén de claves designado (certificados/ecikeystore.p12), para asegurar una comunicación segura a través de HTTPS. Además, se emplea un almacén de confianza específico (certificados/myTrustStore.p12) para verificar la autenticidad de los certificados de los servicios remotos. Esta aplicación se ha construido utilizando el micro-framework Spark Java, empaquetándola como una aplicación ejecutable con el uso del plugin maven-shade-plugin. Este plugin facilita la creación de un archivo JAR autónomo, incluyendo todas las dependencias necesarias para su funcionamiento sin problemas.

## ESCALABILIDAD DE LA ARQUITECTURA DE SEGURIDAD PARA NUEVOS SERVICIOS

La arquitectura de seguridad implementada en esta aplicación ha sido diseñada con un enfoque escalable, facilitando la integración de nuevos servicios de manera eficiente mientras se conservan los rigurosos estándares de seguridad.
1. **Crear un conjunto fresco de certificados**: Se requiere utilizar la herramienta Keytool de Java para generar un nuevo juego de certificados (tanto público como privado) destinados al nuevo servicio. Estos certificados deben ser guardados en el almacén de claves compartido.
2. **Preparar el nuevo servicio**: Es necesario configurar el nuevo servicio para que utilice los certificados generados previamente. Esto implica cargar el almacén de claves y activar la comunicación segura a través del protocolo HTTPS en el código del servicio.
3. **Incluir el certificado en el almacén de confianza**: Se requiere añadir el certificado del nuevo servicio al almacén de confianza compartido. De esta forma, los otros servicios podrán confiar en el nuevo servicio durante las comunicaciones seguras.
4. **Modificar la configuración de los servicios existentes**: Es necesario realizar ajustes en la configuración de los servicios ya en funcionamiento para que confíen en el nuevo certificado incluido en el almacén de confianza. Este proceso podría implicar reiniciar los servicios o recargar la configuración de seguridad durante la ejecución.
5. **Iniciar la comunicación cifrada**: Una vez que todos los servicios hayan establecido la confianza mutua en los certificados, se podrá iniciar la comunicación segura entre ellos mediante el uso de HTTPS.
