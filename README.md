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
10. Ejecutar la app presionando el botón **"Run 'app'"** (triángulo verde) asegurándose de estar en `MainActivity`.
11. Por ultimo, para poder acceder a todas las funciones de la app escribe en el inicio de sesión las siguientes credenciales: **Usuario: juanAdmin@colum.cl** , **Contraseña: 123456**.


### 6. Evidencia de APK firmado y .jks

A continuación se adjuntan las evidencias gráficas del proceso de firma digital y la generación de los archivos entregables en modo **Release**.

**1. Proceso de Generación en Android Studio:**
<img width="543" height="492" alt="image" src="https://github.com/user-attachments/assets/ba6697b3-6690-4113-b0b0-2900daac215e" />

<img width="622" height="490" alt="image" src="https://github.com/user-attachments/assets/15dbe148-7322-4c48-9c6e-3753aaba83b0" />

<img width="620" height="498" alt="image" src="https://github.com/user-attachments/assets/e676ca1d-a59a-45c9-b94d-77a18ffa3b0f" />

<img width="624" height="491" alt="image" src="https://github.com/user-attachments/assets/1b13c7f3-23dc-4fe7-88bb-d0300a77c50a" />


**2. Archivos Generados (.apk y .jks):**
<img width="278" height="171" alt="image" src="https://github.com/user-attachments/assets/c3310ff5-5b4f-4e8a-b81c-977423e46db5" />

<img width="656" height="114" alt="image" src="https://github.com/user-attachments/assets/7a09e162-a474-417d-a4bb-39131c9fd197" />
> [!NOTE]
> **Sobre el archivo APK:** Pese a que el nombre del archivo generado se visualice como `app-release`, este corresponde efectivamente al **APK final**. Al transferirlo a tu dispositivo o subirlo a la nube (Drive), el sistema lo reconocerá correctamente como un archivo `.apk` listo para instalar.

Ejemplo de como se observa en drive:
<img width="350" height="112" alt="image" src="https://github.com/user-attachments/assets/6cf53250-cd08-4f7e-b1bf-d53f181b1eef" />


### 📸 Proceso de Instalación en Dispositivo

A continuación se muestra cómo se visualiza el proceso de instalación manual del archivo **APK** en un dispositivo Android físico.

| 1. Confirmación de Instalación | 2. Proceso de Instalación | 3. App Instalada y Lista |
<img width="399" height="887" alt="image" src="https://github.com/user-attachments/assets/e64fc02f-260c-4784-a8a1-7c5e205edcc0" />

<img width="399" height="890" alt="image" src="https://github.com/user-attachments/assets/02f4cd0e-fbba-4f78-ba4c-6700f04d500e" />

<img width="401" height="891" alt="image" src="https://github.com/user-attachments/assets/0fa281cd-d134-4b73-91ce-3bb43890bed0" />

<img width="401" height="888" alt="image" src="https://github.com/user-attachments/assets/16c33804-c40e-433a-8dae-4b94270c2647" />

<img width="400" height="892" alt="image" src="https://github.com/user-attachments/assets/a29bdf41-b31f-4916-8d30-40a39992be9a" />

> **Nota sobre seguridad:** Al ser una aplicación instalada manualmente (Sideloading) y no descargada desde Google Play Store, es posible que el dispositivo solicite permisos para **"Instalar aplicaciones de fuentes desconocidas"** o muestre una advertencia de **Google Play Protect**. Esto es un comportamiento estándar de seguridad en Android.


### 🛠️ Tecnologías y Librerías

Este proyecto fue desarrollado utilizando un stack tecnológico moderno nativo para Android, integrando herramientas de vanguardia:

* **Lenguaje:** [Kotlin](https://kotlinlang.org/) (100%)
* **Arquitectura:** MVVM (Model-View-ViewModel)
* **Interfaz de Usuario:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
* **Navegación:** Navigation Compose
* **Red y Conectividad:** Retrofit 2 + Gson Converter
* **Asincronismo:** Kotlin Coroutines & StateFlow
* **Persistencia Local:** Jetpack DataStore (Preferences)
* **Carga de Imágenes:** Coil
* **Integración de Hardware y Sensores:**
    * **CameraX / Intents:** Captura de evidencia fotográfica nativa.
    * **Vibrator API (Haptics):** Implementación de feedback háptico (vibración) para alertar al usuario sobre errores de validación en formularios.
* **Calidad y Testing:**
    * **Unit Testing:** JUnit 5, MockK, Coroutines Test.
    * **UI Testing:** Espresso, Compose UI Test (JUnit 4).
