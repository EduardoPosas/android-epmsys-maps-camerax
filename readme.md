# Epmsys

Implementación de el SDK de mapas de Android utilizando Kotlin y Jetpack Compose

## Objetivos

- Implementar el SDK de Android para Google Maps.
- Obtener la ubicación actual por medio del FusedLocationProvider.
- Implementar CameraX para tomar fotografías desde la app y almacenarlas en el dispositivo.
- Almacenar los registros en una base de datos local utilizando Room.
- Utilizar Clean Architecture para todas las funcionalidades.
- Implementar Hilt para inyección de dependencias.


## Tecnologías

- Kotlin
- Jetpack Compose
- Hilt
- CameraX
- Room Database


## Arquitectura

Se implementa la arquitectura limpia para la estructura de la información y se utiliza el patrón de diseño MVVM para la separación de intereses.

### Vistas
Se utiliza la librería Jetpack Compose y se definen las diferentes pantallas que conforman la aplicación. En este caso, se tiene la pantalla para mostrar el mapa y los registros capturados, la pantalla para crear nuevos registros y la vista previa para capturar imágenes.

<img alt="views_architecture.png" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Fviews_architecture.png" width="500"/>

### State Holders
Estan encargados de elevar el estado y proveer a los composables del estado que requieran (hace que los composables sean stateless). Aquí se definen los data class con la estructura del estado y se comparten con las vista por medio de objetos observables, en este caso utilizando `MutableStateFlows` y `MutableStateOf` .

<img alt="viewmodels_architecture.png" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Fviewmodels_architecture.png" width="500"/>

### Capa de dominio y datos
En la capa de domino se implementan las interfaces o contratos que definen la estructura de los casos de uso, repositorios y modelos que serán utilizados por la capa de datos para interactura con la base de datos, con los proveedores de la ubicación o con la funcionalidad de la camara.

<img alt="data_architecture.png" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Fdata_architecture.png" width="500"/>

### Inyección de dependencias
Se implementa la librería Hilt para proveer a la aplicación las diferentes dependencias que se requieren para implementar las funcionalidades. Por ejemplo, la dependencia para acceder a la ubicación actual del dispositivo (`FusedLocationProviderClient`) o la dependencia para implementar el caso de uso para tomar y guardar una imágen (`ImageCapure`)

## Flujo de la aplicación

- Pantalla de inicio de la aplicación.

  <img alt="one_map_initial_screen_min.jpg" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Fone_map_initial_screen_min.jpg" width="250"/>

- Solicitar permisos para la acceder a la ubicación del dispositivo.

  <img alt="two_request_location_permission_min.jpg" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Ftwo_request_location_permission_min.jpg" width="250"/>

- Pantalla de inicio, con los permisos garantizados.

  <img alt="three_map_location_granted_min.jpg" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Fthree_map_location_granted_min.jpg" width="250"/>

- Pantalla del formulario para crear nuevo registro.

  <img alt="four_new_register_initial_form_min.jpg" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Ffour_new_register_initial_form_min.jpg" width="250"/>

- Validación de los datos.

  <img alt="five_new_register_validation_min.jpg" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Ffive_new_register_validation_min.jpg" width="250"/>

- Solicitud de permisos para acceder a la cámara del dispositivo.

  <img alt="six_request_camera_permission_min.jpg" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Fsix_request_camera_permission_min.jpg" width="250"/>

- Vista previa de la cámara

  <img alt="seven_camera_preview_min.jpg" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Fseven_camera_preview_min.jpg" width="250"/>

- Pantalla de nuevo registro completada.

  <img alt="eight_new_register_fullfilled_min.jpg" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Feight_new_register_fullfilled_min.jpg" width="250"/>

- Pantalla del mapa con los registros creados.

  <img alt="nine_map_view_with_markers_min.jpg" src="app%2Fsrc%2Fmain%2Fres%2Fdrawable%2Fnine_map_view_with_markers_min.jpg" width="250"/>


## Recursos

[Android CameraX](https://developer.android.com/media/camera/camerax)

[Permissions on Android](https://developer.android.com/guide/topics/permissions/overview)

[Build location aware apps](https://developer.android.com/develop/sensors-and-location/location)

[Android maps compose](https://github.com/googlemaps/android-maps-compose) 

