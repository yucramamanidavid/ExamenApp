# Juego 3 en Raya

Este es un proyecto de un juego de 3 en raya (Tic-Tac-Toe) desarrollado en **Android** siguiendo la arquitectura **MVVM** (Model-View-ViewModel).

## Tabla de Contenidos

- [Descripción del Proyecto](#descripción-del-proyecto)
- [Arquitectura del Sistema](#arquitectura-del-sistema)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Instalación y Configuración](#instalación-y-configuración)
- [Contribución](#contribución)
- [Licencia](#licencia)

---

## Descripción del Proyecto

Este proyecto implementa un juego clásico de 3 en raya en Android, utilizando el patrón de arquitectura **MVVM**. El objetivo es demostrar una correcta separación de responsabilidades mediante el uso de ViewModel para la gestión de la lógica del juego, Repositorio para el acceso a datos y una interfaz gráfica interactiva para el usuario.

---

## Arquitectura del Sistema

Este proyecto sigue la arquitectura **MVVM**, que divide el sistema en tres componentes principales:

1. **Modelo**: Define las entidades de datos y el acceso a la base de datos.
2. **Vista**: La interfaz gráfica de usuario (UI), que interactúa con el ViewModel para obtener los datos.
3. **ViewModel**: Actúa como intermediario entre la Vista y el Modelo, manejando la lógica del juego y proporcionando datos a la Vista.

### Diagrama de la Arquitectura

Aquí tienes un diagrama que muestra la arquitectura del sistema:

```mermaid
graph TD
    A[MainActivity] -->|Interactúa con| B[TicTacToeScreen]
    B -->|Envía comandos a| C[TicTacToeViewModel]
    C -->|Pide datos a| D[GameResultRepository]
    D -->|Obtiene datos de| E[GameResultDao]
    D -->|Usa base de datos| F[AppDatabase]
    E -->|Contiene resultados del juego| G[GameResult]
