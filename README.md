# Wifi Panamá


En esta aplicación usted puede conocer los puntos donde se ofrece Wifi gratuito en Panamá con sus respectivos datos de ubicación, el tipo de sitio en el que se encuentra, la latitud, la longitud, la dirección donde se encuentra y el área de cobertura disponible.

Esta aplicación le permite consultar la ubicación de todos los puntos de Wifi en un mapa base de OpenStreet. Además cuenta con la funcionalidad de ruteo que usa la tecnología de Esri, por medio del cual le permite una vez iniciada la aplicación cargar una ruta desde su ubicación a uno de los puntos de Wifi.

Adicionalmente la aplicación muestra las indicaciones de cada ruta trazada, y por medio de un asistente de voz se describe la sección seleccionada. 


Esta aplicación fue creada por el Semillero de Innovación Geografica (SIG), apoyado por Esri Colombia y usa ArcGIS Online para desarrolladores, y el Runtime para Android., para mayor información, no olvide seguirnos en la cuenta de Twitter @geo_geeks o en el portal de desarrolladores http://desarrolladores.esri.co.

## Configuración

Para que la aplicación sea funcional es necesario contar con un los Sistemas de Información Geográfica de ESRI Colombia por medio de la plataforma ArcGis, de ahí se toman lo datos a mostrar

Se debe generar el codigo de Google Cloud Messaging para habilitar la recepcion de notificaciones en el dispositivo Android

   ```java
// Create a new application at https://developers.arcgis.com/en/applications
private static final String AGO_CLIENT_ID = "...";

// The project number from https://code.google.com/apis/console
private static final String GCM_SENDER_ID = "...";
```

## Implementación

- Se requiere que los servicios de Arcgis se encuentren habilitados y públicos
- Agregue el proyecto en su entorno de desarrollo para aplicaciones moviles

## Licenciamiento

Copyright 2014 Esri

Licenciado bajo la licencia Apache, Versión 2.0; usted no puede utilizar este archivo excepto en conformidad con la Licencia. Usted puede obtener una copia de la licencia en

http://www.apache.org/licenses/LICENSE-2.0

A menos que lo requiera la ley o se acuerde por escrito, el software distribuido bajo la licencia se distribuye "TAL CUAL", SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ya sea expresa o implícita. Consulte la licencia para los permisos específicos de gobierno de idiomas y limitaciones en la licencia. 

Una copia de la licencia está disponible en el archivo license.txt del repositorio.
