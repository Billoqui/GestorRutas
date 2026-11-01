# Gestor de Rutas Adaptable

Sistema modular de gestión y cálculo de rutas desarrollado con **Java 21, Scala 3.3.6 y PostgreSQL**, enfocado en la selección adaptativa de algoritmos, la arquitectura limpia y la extensibilidad mediante plugins.

## Descripción del proyecto

Gestor de Rutas Adaptable es un proyecto académico de ingeniería de software que representa mapas mediante grafos y selecciona algoritmos de búsqueda de rutas según el contexto de ejecución y criterios de rendimiento establecidos.

El sistema está diseñado para integrar diferentes estrategias de cálculo mediante una interfaz común, permitiendo que algoritmos implementados en distintos lenguajes compatibles con la JVM trabajen conjuntamente.

## Objetivos principales

- Representar mapas mediante grafos compuestos por nodos y conexiones.
- Implementar diferentes estrategias de cálculo de rutas.
- Descubrir y cargar plugins dinámicamente mediante Reflection de Java.
- Seleccionar algoritmos según las condiciones y necesidades de ejecución.
- Integrar Java y Scala dentro de una misma aplicación.
- Almacenar información del grafo, rutas y métricas de ejecución en PostgreSQL.
- Aplicar principios de programación orientada a objetos, arquitectura limpia y pruebas automatizadas.

## Tecnologías utilizadas

| Tecnología | Versión / Uso |
|---|---|
| Java | JDK 21 |
| Scala | 3.3.6 |
| Maven | 3.9.11 (Maven Wrapper) |
| PostgreSQL | Base de datos |
| JDBC | Conexión con la base de datos |
| JUnit | Pruebas automatizadas |
| Git y GitHub | Control de versiones |

## Arquitectura del proyecto

El proyecto utiliza una arquitectura modular administrada mediante Maven.

```text
GestorRutas/
│
├── plugin-api/       # Interfaces y contratos de plugins
├── domain/           # Entidades y reglas del dominio
├── adaptive-core/    # Descubrimiento y selección adaptativa
├── plugins-java/     # Algoritmos implementados en Java
├── plugins-scala/    # Algoritmos implementados en Scala
├── persistence/      # Acceso a PostgreSQL mediante JDBC
├── launcher/         # Punto de entrada de la aplicación
│
├── .mvn/             # Configuración de Maven Wrapper
├── mvnw
├── mvnw.cmd
├── pom.xml           # Proyecto Maven principal
└── README.md
```

### Responsabilidad de los módulos

- **plugin-api:** Define los contratos comunes que deben cumplir las estrategias.
- **domain:** Contiene las entidades principales, estructuras del grafo y reglas del dominio.
- **adaptive-core:** Gestiona el descubrimiento, validación, construcción y selección de plugins.
- **plugins-java:** Contiene los algoritmos de cálculo de rutas desarrollados en Java.
- **plugins-scala:** Contiene estrategias adicionales implementadas en Scala.
- **persistence:** Gestiona la conexión y persistencia de información mediante JDBC y PostgreSQL.
- **launcher:** Coordina los componentes y proporciona el punto de entrada de la aplicación.

## Estrategias de cálculo de rutas previstas

La implementación inicial contempla las siguientes estrategias:

- **Dijkstra:** Encuentra rutas óptimas según los pesos de las conexiones.
- **A\* (A-Star):** Utiliza una heurística para orientar la búsqueda hacia el destino.
- **Estrategia implementada en Scala:** Incorporará un algoritmo adicional de cálculo de rutas.

El motor adaptativo evaluará el contexto y los criterios disponibles para seleccionar la estrategia más adecuada.

## Base de datos

PostgreSQL almacenará información relacionada con:

- Nodos y conexiones del grafo.
- Información de rutas.
- Métricas de ejecución.
- Historial de selección y ejecución de algoritmos.

Los grafos se cargarán en memoria para ejecutar los algoritmos de búsqueda sin depender de consultas constantes a la base de datos.

## Instalación y ejecución

### Requisitos

- JDK 21.
- Git.
- Conexión a Internet para descargar dependencias de Maven.
- PostgreSQL para las funcionalidades relacionadas con persistencia.

### Clonar el repositorio

```bash
git clone https://github.com/Billoqui/GestorRutas.git
cd GestorRutas
```

### Validar el proyecto Maven

En Windows:

```powershell
.\mvnw.cmd validate
```

En Linux o macOS:

```bash
./mvnw validate
```

## Estado del desarrollo

**Etapa actual: configuración inicial del proyecto.**

- [x] Configuración del entorno de desarrollo.
- [x] Instalación y configuración de Maven Wrapper.
- [x] Creación de la estructura modular Maven.
- [x] Inicialización del repositorio Git.
- [x] Creación del repositorio en GitHub.
- [ ] Implementación de la API de estrategias.
- [ ] Implementación del modelo del dominio.
- [ ] Desarrollo de algoritmos de rutas en Java.
- [ ] Desarrollo de algoritmo de rutas en Scala.
- [ ] Implementación del sistema de plugins mediante Reflection.
- [ ] Implementación de selección adaptativa.
- [ ] Integración con PostgreSQL.
- [ ] Pruebas automatizadas y documentación técnica.

## Repositorio

GitHub: https://github.com/Billoqui/GestorRutas

---

**Proyecto académico individual — Ingeniería de Software**

Desarrollado para aplicar arquitectura modular, programación orientada a objetos, interoperabilidad Java–Scala, descubrimiento dinámico de plugins y selección adaptativa de algoritmos.
