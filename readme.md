# Prestamigos Backend

Prestamigos es una aplicación para gestionar deudas y préstamos. Este proyecto ofrece toda la funcionalidad necesaria para el correcto uso de la aplicación: 

  - Acceso a los recursos a través de peticiones HTTP
  - Conexión con la base de datos
  - Respuestas en formato JSON
 
También se ha incluído una interfaz web usando los componentes de Java EE (servlets, jsp...) sin profundizar demasiado, ya que en el futuro se reemplazará por Angular 2 ya que permite que el cliente gestione toda la UI limitándose el servidor a servir peticiones.

### Tecnologías

Se usan las siguientes librerías y framework de Java:

* [Spring](http://spring.io) - Framework usado para facilitar la construcción de aplicaciones web sobre Java EE.
* [Hibernate](http://hibernate.org/orm/) - ORM que facilita el mapeo de atributos entre la base de datos y los objetos de la aplicaci´ón  
* [Jackson](https://github.com/FasterXML/jackson) - Librería que facilita la conversión de objetos a JSON y viceversa.
* [log4j](https://logging.apache.org/log4j/2.x/) - Librería para el manejo de logs.

Además para la parte web se hace uso de JSP, JSTL, CSS y jQuery.

### Configuración del proyecto
Se necesitar tener gradle instalado y configurado.

Una vez importado el git, si es necesario, convertir el proyecto a Gradle (Eclipse) y ejecutar los siguientes comandos de gradle para bajarse las dependencias necesarias:

```sh
clean eclipse
```

Una vez hecho, abir el fichero conexion.properties en src/main/resources, cuyo contenido es:
```sh
bbdd=jdbc-mysql-//URL-3306/ESQUEMA
password=secret
usuario=user

correo_servidor=smtp.email.com
correo_usuario=info@email.com
correo_password=secret
```
Y reemplazar:
* bbdd: Conexión JDBC. Reemplazar los : por -
* password: Contraseña del usuario de la base de datos
* usuario: Nombre del usuario de la base de datos
* correo_servidor: Datos del servidor de correo a usar
* correo_usuario: Cuenta de correo a usar para enviar correos.
* correo_password: Contraseña de la cuenta.

Para generar el WAR y desplegarlo en un servidor de aplicaciones simplemente ejecutar:

```sh
war
```

Para acceder a la aplicación a través de un navegador, una vez desplegado, introducir como url:
```sh
http://URL:PUERTO/Prestamigos-Backend-Java
```
Por ejemplo, en local:
```sh
http://localhost:8080/Prestamigos-Backend-Java
```
### Estructura de los paquetes

El proyecto sigue un patrón MVC (Modelo Vista Controlador). 

| Paquete | Descripción |
| ------ | ------ |
| es.jma.email | Clases para el envío de correos electrónicos |
| es.jma.prestamos.config | En el ApplicationContextConfig se encuentran los Beans necesitados por los controladores |
| es.jma.prestamos.constantes | Clases para guardar constantes |
| es.jma.prestamos.controllers.rest | Controladores REST. Punto de acceso a la aplicación a través de peticiones HTTP  |
| es.jma.prestamos.controllers.web | Controlador para la parte que se accede a través de la web|
| es.jma.prestamos.daos | Interfaces de los DAOs |
| es.jma.prestamos.daos.impl | Implementación de los DAOs |
| es.jma.prestamos.dominio | Clases POJO |
| es.jma.prestamos.enums | Enum de Java necesarios |
| es.jma.prestamos.exceptiones | Excepciones personalizadas |
| es.jma.prestamos.servicios | Interfaces de los servicios |
| es.jma.prestamos.servicios.impl | Implementación de los servicios |
| es.jma.prestamos.utils | Clases de utilidad |
| es.jma.prestamos.validadores | Clases de utilidad para validar |