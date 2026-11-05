# Arquitectura del Gestor de Rutas Adaptable

## 1. Descripción general

Gestor de Rutas Adaptable es un sistema modular desarrollado con Java 21, Scala 3 y PostgreSQL que permite calcular rutas entre diferentes puntos de un grafo.

El sistema utiliza una arquitectura basada en estrategias intercambiables. Cada algoritmo implementa un contrato común, lo que permite que el motor adaptativo seleccione y ejecute diferentes implementaciones sin depender de sus detalles internos.

Además, incorpora un monitor de métricas y reglas de adaptación para evaluar las ejecuciones y decidir cuándo utilizar una estrategia alternativa.

## 2. Arquitectura modular

El proyecto está organizado en los siguientes módulos:

- **plugin-api:** define el contrato común que deben implementar las estrategias.
- **domain:** contiene las entidades y estructuras necesarias para representar grafos, nodos y conexiones.
- **adaptive-core:** administra la selección y ejecución de estrategias, supervisa métricas y aplica reglas de adaptación.
- **plugins-java:** contiene algoritmos de búsqueda implementados en Java.
- **plugins-scala:** contiene una implementación alternativa del algoritmo de búsqueda en Scala.
- **persistence:** gestiona la conexión y las operaciones de persistencia mediante JDBC y PostgreSQL.
- **launcher:** integra los módulos y permite iniciar la aplicación.

### 2.1. Responsabilidad de los módulos

Cada módulo cumple una función específica:

1. La API establece las reglas de comunicación entre el motor y los algoritmos.
2. El dominio proporciona las estructuras que representan el problema.
3. Los plugins implementan los algoritmos de búsqueda.
4. El núcleo adaptativo administra la selección y supervisión de estrategias.
5. La persistencia registra ejecuciones y decisiones.
6. El lanzador integra los componentes y ejecuta el sistema.

## 3. Contrato de estrategias

El módulo `plugin-api` define el contrato común que permite intercambiar algoritmos.

### 3.1. Strategy

Interfaz genérica que establece el comportamiento obligatorio de cada estrategia.

Sus métodos principales son:

- `id()`: identifica la estrategia.
- `supports(Context)`: determina si puede trabajar con un contexto.
- `execute(I)`: ejecuta el algoritmo y devuelve un resultado.

Gracias a este contrato, el motor adaptativo puede trabajar con distintas implementaciones sin conocer sus detalles internos.

### 3.2. Context

Registro que almacena la información necesaria para evaluar la compatibilidad de una estrategia:

- Origen.
- Destino.
- Criterio de optimización.
- Permiso para utilizar pesos negativos.

### 3.3. Result

Registro genérico que representa el resultado de una ejecución. Contiene información sobre:

- Estado de éxito.
- Valor devuelto.
- Mensaje descriptivo.
- Tiempo de ejecución.

## 4. Estrategias de búsqueda

El proyecto incorpora tres estrategias:

### 4.1. Dijkstra en Java

Calcula rutas considerando los pesos de las conexiones del grafo. Se utiliza para encontrar caminos de costo mínimo cuando se cumplen las condiciones del algoritmo.

### 4.2. A* en Java

Utiliza una función heurística para orientar la búsqueda hacia el destino, con el objetivo de encontrar rutas de manera eficiente.

### 4.3. Dijkstra en Scala

Implementa una alternativa de búsqueda mediante Scala 3. Esta estrategia comparte el contrato de la API, permitiendo su integración con el motor desarrollado en Java.

## 5. Motor adaptativo

El módulo `adaptive-core` administra la selección, ejecución y supervisión de las estrategias.

Su funcionamiento general es:

1. Recibe la información necesaria para calcular una ruta.
2. Selecciona una estrategia compatible.
3. Ejecuta el algoritmo.
4. Registra las métricas de ejecución.
5. Evalúa las reglas de adaptación.
6. Si se cumple una condición, selecciona una estrategia alternativa.
7. Registra la decisión tomada.

### 5.1. Monitorización de métricas

El sistema supervisa información como:

- Cantidad de ejecuciones.
- Cantidad de errores.
- Tiempo promedio de ejecución.

Estas métricas permiten evaluar el comportamiento de las estrategias.

### 5.2. Reglas de adaptación

Actualmente se consideran condiciones relacionadas con el tiempo de ejecución y el porcentaje de errores.

La configuración se encuentra en:

`adaptive-core/src/main/resources/adaptive-config.json`

El umbral de tiempo puede modificarse para probar el cambio automático entre estrategias.

## 6. Persistencia de información

El módulo `persistence` utiliza JDBC para establecer la comunicación con PostgreSQL.

El sistema registra dos tipos principales de información:

### 6.1. Ejecuciones

La tabla `ejecuciones` almacena:

- Identificador.
- Nombre de la estrategia.
- Tiempo de ejecución.
- Estado de éxito.
- Fecha de registro.

El repositorio permite realizar operaciones CRUD:

- Crear registros.
- Consultar registros.
- Actualizar registros.
- Eliminar registros.

### 6.2. Decisiones adaptativas

La tabla `decisiones_adaptativas` almacena:

- Estrategia inicial.
- Estrategia alternativa.
- Motivo de la decisión.
- Indicador de cambio de estrategia.
- Fecha de registro.

Esto permite conservar un historial de las decisiones tomadas por el motor adaptativo.

### 6.3. Seguridad de conexión

La contraseña de PostgreSQL se obtiene mediante la variable de entorno:

`GESTOR_RUTAS_DB_PASSWORD`

De esta manera, las credenciales no necesitan estar escritas directamente en el código fuente.

## 7. Interacción entre los componentes

El flujo general del sistema es:

1. El lanzador inicia la aplicación.
2. El motor adaptativo recibe la solicitud de búsqueda.
3. El motor consulta las estrategias disponibles.
4. La estrategia seleccionada ejecuta el algoritmo.
5. El monitor registra las métricas.
6. Las reglas de adaptación evalúan el resultado.
7. Si corresponde, se ejecuta una estrategia alternativa.
8. La persistencia registra las ejecuciones y decisiones.

## 8. Pruebas y verificaciones

Durante el desarrollo se han realizado pruebas y verificaciones para comprobar:

- El funcionamiento del contrato de estrategias.
- La ejecución de los algoritmos de búsqueda.
- La integración entre Java y Scala.
- El funcionamiento del motor adaptativo.
- La conexión con PostgreSQL.
- Las operaciones CRUD.
- El registro de decisiones adaptativas.
- La compilación y verificación de los módulos mediante Maven.

También se comprobó un escenario en el que Dijkstra supera el umbral de tiempo configurado y el motor selecciona Dijkstra implementado en Scala como alternativa.

La verificación general del proyecto se realizó mediante:

```powershell
.\mvnw.cmd clean verify
```

El resultado obtenido fue `BUILD SUCCESS`.

## 9. Estado de la arquitectura

La arquitectura modular, el contrato de estrategias, los algoritmos de búsqueda, el motor adaptativo y la persistencia se encuentran implementados e integrados.

La documentación técnica y la revisión de los entregables finales continúan como parte del proceso de desarrollo.