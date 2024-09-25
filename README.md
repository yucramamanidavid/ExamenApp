# Juego 3 en Raya

Este proyecto implementa el juego clásico de 3 en raya (Tic-Tac-Toe), desarrollado en **Kotlin** utilizando el patrón de arquitectura **MVVM** (Model-View-ViewModel). La aplicación guarda los resultados de las partidas en una base de datos local utilizando **Room**. La finalidad del proyecto es mostrar cómo una aplicación estructurada bajo el patrón MVVM mejora la mantenibilidad y escalabilidad del código.

## Tabla de Contenidos

1. [Introducción](#introducción)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Estructura del Proyecto](#estructura-del-proyecto)
4. [Detalles de los Componentes](#detalles-de-los-componentes)
5. [Cómo Configurar y Ejecutar el Proyecto](#cómo-configurar-y-ejecutar-el-proyecto)
6. [Características Principales](#características-principales)
7. [Diagrama de la Arquitectura](#diagrama-de-la-arquitectura)
8. [Contribuciones](#contribuciones)


---

## Introducción

El propósito de este proyecto es desarrollar una aplicación sencilla de un **juego de 3 en raya** con enfoque en la separación de responsabilidades y buenas prácticas utilizando la arquitectura **MVVM**. El patrón MVVM permite una clara distinción entre la capa de datos (modelo), la interfaz gráfica (vista) y la lógica de negocio (ViewModel), lo que facilita el mantenimiento y la escalabilidad del código.

La aplicación también cuenta con un sistema de persistencia utilizando **Room**, una librería de persistencia de datos local, para guardar los resultados de las partidas y poder consultarlos posteriormente.

---

## Arquitectura del Sistema

La aplicación sigue el patrón de diseño **MVVM (Model-View-ViewModel)**, que se descompone en tres componentes principales:

- **Model (Modelo)**: Responsable de la gestión de los datos. Incluye las clases para la base de datos, los DAO (Data Access Objects) y las entidades que representan los datos, en este caso, los resultados de las partidas.
  
- **View (Vista)**: Se encarga de la interfaz de usuario. Esta capa incluye el código que genera la UI del juego, como el tablero de 3 en raya y las interacciones de los jugadores con el juego.

- **ViewModel**: Actúa como intermediario entre la vista y el modelo. Gestiona la lógica de negocio, actualiza la vista y se comunica con el repositorio para obtener y almacenar los datos.

---

## Estructura del Proyecto

El proyecto está organizado en las siguientes carpetas y archivos:

```plaintext
main/
  └── java/
      └── pe.edu.upeu.juego3enraya/
          ├── model/
          │    ├── AppDatabase.kt
          │    ├── GameResult.kt
          │    └── GameResultDao.kt
          ├── repository/
          │    └── GameResultRepository.kt
          ├── ui/
          │    ├── view/
          │    │    └── TicTacToeScreen.kt
          │    └── theme/           // Aquí se define el tema de la aplicación.
          ├── viewmodel/
          │    ├── TicTacToeViewModel.kt
          │    └── TicTacToeViewModelFactory.kt
          └── MainActivity.kt
```
## Detalles de los Componentes

### 1. **Model**
   - **`GameResult.kt`**: Representa la entidad que guarda el resultado de una partida. Esta clase define los campos que se almacenan en la base de datos, como los jugadores y el resultado del juego.
   - **`GameResultDao.kt`**: Es la interfaz de acceso a los datos. Define los métodos de consulta y almacenamiento de los resultados en la base de datos. Utiliza anotaciones de Room para realizar operaciones de CRUD (Create, Read, Update, Delete).
   - **`AppDatabase.kt`**: Configura la base de datos utilizando Room. Define la instancia de la base de datos y conecta el DAO (`GameResultDao`) con la aplicación.

### 2. **Repository**
   - **`GameResultRepository.kt`**: Gestiona la lógica de acceso a los datos. Interactúa con el DAO para obtener los resultados de las partidas y exponerlos al ViewModel. El repositorio es el responsable de decidir de dónde provienen los datos (base de datos, red, etc.).

### 3. **View**
   - **`TicTacToeScreen.kt`**: Representa la interfaz gráfica del juego de 3 en raya. Muestra el tablero y gestiona la interacción con el usuario. Se comunica con el ViewModel para obtener el estado del juego y actualizar la UI.
   - **`MainActivity.kt`**: La actividad principal de la aplicación que aloja la vista del juego.

### 4. **ViewModel**
   - **`TicTacToeViewModel.kt`**: Gestiona la lógica del juego de 3 en raya. Controla el flujo de la partida, valida las jugadas y actualiza el estado de la vista. Se comunica con el repositorio para almacenar los resultados de las partidas.
   - **`TicTacToeViewModelFactory.kt`**: Proporciona una instancia de `TicTacToeViewModel` cuando se necesita. Esto es útil para inyectar dependencias en el ViewModel, como el repositorio.

---

## Cómo Configurar y Ejecutar el Proyecto

### Prerrequisitos

1. **Android Studio**: Instala la última versión.
2. **Kotlin**: Este proyecto está escrito en Kotlin.
3. **Room**: Librería utilizada para la persistencia de datos local.

### Pasos para Ejecutar el Proyecto

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/tu-usuario/Juego3EnRaya.git
2. **Abrir el proyecto en Android Studio**:  
   Clona este repositorio o descarga el código fuente y ábrelo en Android Studio.

3. **Sincronizar dependencias**:  
   Ve a `Build` > `Sync Project with Gradle Files` para sincronizar todas las dependencias necesarias del proyecto.

4. **Ejecutar la aplicación**:  
   Una vez sincronizado el proyecto, puedes ejecutarlo en un emulador o en un dispositivo físico conectado. Para hacerlo, selecciona el dispositivo en la barra superior de Android Studio y haz clic en el botón `Run` o presiona `Shift + F10`.
---
## Características Principales

- **Juego Local de 3 en Raya**: Permite que dos jugadores compitan en una partida local.
- **Persistencia de Datos**: Los resultados de cada partida se guardan en una base de datos local (Room) para que puedan consultarse posteriormente.
- **Arquitectura MVVM**: El proyecto sigue la arquitectura MVVM para separar la lógica de negocio, la interfaz gráfica y la gestión de datos.
- **Tema Personalizado**: La aplicación cuenta con un tema definido en la carpeta `theme`, lo que permite un diseño consistente en toda la interfaz.
---
## Diagrama de la Arquitectura

El siguiente diagrama muestra la arquitectura del sistema basada en MVVM (Model-View-ViewModel):

```mermaid
graph TD
    A[MainActivity] -->|Interactúa con| B[TicTacToeScreen]
    B -->|Envía comandos a| C[TicTacToeViewModel]
    C -->|Pide datos a| D[GameResultRepository]
    D -->|Obtiene datos de| E[GameResultDao]
    D -->|Usa base de datos| F[AppDatabase]

    E -->|Contiene resultados del juego| G[GameResult]
    
    style A fill:#f9f,stroke:#333,stroke-width:2px
    style B fill:#bbf,stroke:#333,stroke-width:2px
    style C fill:#bbf,stroke:#333,stroke-width:2px
    style D fill:#bbf,stroke:#333,stroke-width:2px
    style E fill:#f96,stroke:#333,stroke-width:2px
    style F fill:#f96,stroke:#333,stroke-width:2px
    style G fill:#f96,stroke:#333,stroke-width:2px
