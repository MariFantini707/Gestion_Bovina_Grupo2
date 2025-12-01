# Gestión bovina

## Descripción
Este proyecto consiste en el desarrollo de una aplicación móvil nativa creada en Android Studio, utilizando el lenguaje de programación Kotlin y el toolkit moderno de interfaz Jetpack Compose, todo estructurado bajo el patrón de arquitectura de software MVVM (Model-View-ViewModel). El propósito principal del sistema es implementar una gestión digital integral para el ganado bovino, permitiendo registrar y realizar conteos de animales en estricto cumplimiento con las normativas del Servicio Agrícola y Ganadero (SAG), optimizando así la trazabilidad y el control en el campo.

El sistema ofrece una experiencia completa que permite a los usuarios gestionar el inventario ganadero, conectándose a un Backend propio en Node.js con MongoDB Atlas para la persistencia en la nube y utilizando DataStore para datos locales. Además de la captura fotográfica con la cámara, se ha integrado el recurso nativo de vibración (respuesta háptica) como mecanismo de alerta; esta función se activa automáticamente en el formulario de registro bovino cuando el sistema detecta un llenado incorrecto o campos faltantes, proporcionando una retroalimentación física inmediata al usuario para corregir el error.

## Estudiantes
- Oscar Abud Palma
- María Victoria Fantini

## Funcionalidades Implementadas

- **Funcionalidad 1: Registro de Vacas con Soporte Multimedia y Háptico:** Permite registrar una vaca ingresando todas sus características obligatorias (DIIO, raza, edad, estado, etc.) en cumplimiento con la normativa del Servicio Agrícola y Ganadero (SAG). Este módulo integra el uso de la cámara nativa para capturar y adjuntar evidencia fotográfica del animal, e incorpora respuesta háptica (vibración) para alertar físicamente al usuario en caso de errores de validación durante el llenado del formulario.
  

- **Funcionalidad 2: Dashboard de Métricas y Contadores:** Presenta un panel visual con indicadores en tiempo real que incluye un contador histórico del total de vacas en el sistema y un contador diario específico para las vacas registradas durante la jornada actual, facilitando el control rápido del flujo de trabajo (dispuesta a cambios).
  

- **Funcionalidad 3: Gestión de Inventario y Filtrado por Estado:** Ofrece una vista detallada del listado de vacas registradas, permitiendo al usuario visualizar la información clave de cada animal. Incluye un sistema de filtrado inteligente para alternar la vista entre vacas "Activas" (presentes en el predio) y "Desactivadas" (historial de bajas), facilitando la búsqueda y organización.
  

- **Funcionalidad 4: Edición y Control de Bajas (Ciclo de Vida):** Permite la gestión completa del registro animal, habilitando la actualización (Update) de los datos de cualquier vaca en caso de errores o cambios en sus características. Además, incorpora la funcionalidad de desactivación (Soft Delete) para dar de baja animales del sistema activo en situaciones críticas (como venta, enfermedad o deceso), manteniendo la integridad histórica de los datos.


### 4. Endpoints Utilizados

La aplicación móvil consume una API REST propia (Backend Node.js) para la persistencia y gestión de datos. A continuación se detallan los endpoints utilizados por cada módulo de la aplicación:

| Pantalla / Funcionalidad | Método | Endpoint | Descripción |
| :--- | :---: | :--- | :--- |
| **Inicio de Sesión** | `POST` | `/api/auth/login` | Verifica las credenciales del usuario y retorna un Token de sesión. |
| **Home (Dashboard)** | `GET` | `/api/vacas` | Obtiene el listado completo de vacas activas para calcular métricas y mostrarlas. |
| **Vacas Desactivadas** | `GET` | `/api/vacas/desactivadas` | Obtiene el historial de vacas que han sido dadas de baja del sistema. |
| **Registrar Vaca** | `POST` | `/api/vacas` | Envía los datos del formulario (incluyendo DIIO y foto) para crear un nuevo registro. |
| **Editar Vaca** | `PATCH` | `/api/vacas/:id` | Actualiza la información de una vaca específica identificada por su ID único. |
| **Eliminar Vaca** | `DELETE` | `/api/vacas/:id` | Realiza una eliminación lógica (cambio de estado) del registro en la base de datos. |

> **Nota:** El parámetro `:id` corresponde al **DIIO**, el cual funciona como el identificador único del animal en la base de datos MongoDB.
>
> 

## Pasos para Ejecutar el Proyecto
  
### 5. Pasos para Ejecutar

Para el correcto funcionamiento del sistema, es necesario levantar primero el servidor (Backend) y posteriormente la aplicación móvil.

#### Parte A: Backend (API Microservicio)

1.  Abrir la carpeta del proyecto backend en **Visual Studio Code**.
2.  Abrir una nueva terminal en el editor.
3.  Instalar las dependencias del proyecto ejecutando el siguiente comando:
    ```bash
    npm install
    ```
4.  Iniciar el servidor ejecutando:
    ```bash
    npm start
    ```
    > **Nota:** Verifique en la consola que aparezca el mensaje de "Conexión exitosa a la base de datos" y que el servidor esté corriendo.

#### Parte B: Aplicación Móvil (Android Studio)

1.  Abrir la aplicación **Android Studio**.
2.  Presionar en la sección superior izquierda el nombre de la app (o proyecto) que está abierto por defecto.
3.  Seleccionar la opción **"Clone repository..."**.
4.  En la sección donde está escrito "URL", colocar el siguiente enlace:
    ```text
    [https://github.com/MariFantini707/Gestion_Bovina_Grupo2.git](https://github.com/MariFantini707/Gestion_Bovina_Grupo2.git)
    ```
5.  Presionar **"Clone"** para que el archivo sea clonado correctamente.
6.  Sincronizar las dependencias de la app para que no existan errores.
    * *Nota: Aparecerá una notificación en la parte superior recomendando realizar un "Sync project with gradle files" o un botón "Sync Now". Presiónelo.*
7.  Ir al apartado de **"Device Manager"**.
8.  **IMPORTANTE - Configuración del Emulador:**
    * Seleccionar o crear un emulador con **API 34** o **API 35**.
    * **Advertencia:** No utilizar la API 36, ya que puede generar conflictos con las pruebas.
9.  Una vez que se visualice la pantalla del emulador, presionar el botón de encendido (**Play**).
10. Por último, ejecutar la app presionando el botón **"Run 'app'"** (triángulo verde) asegurándose de estar en `MainActivity`.
   
  
