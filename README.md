# Prueba Técnica HolaLuz

Hecho con java 17, Maven 3.9.9 y Spring Boot 3.4.2

## Instalación

Una vez clonado el Repositorio, basta con hacer maven install y arrancar la clase main TestApplication.

Al arrancar se levantará un Servicio Rest en localhost:8080 cuyo endpoint seria 
```bash
http://localhost:8080/api/readings/readFile/{nombreArchivo}
```
Siendo {nombreArchivo} el nombre del archivo a comprobar.

Los archivos a comprobar deben de guardarse en la ruta
```bash
src/main/resources/files
```

Si el servicio procesa correctamente los datos del archivo de entrada, se generará un archivo CSV 

que contendrá las lecturas sospechosas. La ubicación de los archivos resultantes es la siguiente
```bash
src/main/resources/files/results
```
