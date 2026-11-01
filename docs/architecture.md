
# Arquitectura del Gestor de Rutas Adaptable

## 1. Descripción general

El Gestor de Rutas Adaptable es un sistema que permite calcular rutas entre diferentes puntos de un grafo.

El sistema utiliza una arquitectura basada en estrategias intercambiables, permitiendo incorporar distintos algoritmos de búsqueda sin modificar el núcleo principal.

## 2. Arquitectura modular

El proyecto está dividido en los siguientes módulos:

- **plugin-api:** define el contrato que deben cumplir los algoritmos.
- **domain:** contiene las entidades y modelos principales del problema.
- **adaptive-core:** administra la selección y ejecución de estrategias.
- **plugins-java:** contiene algoritmos implementados en Java.
- **plugins-scala:** contiene algoritmos implementados en Scala.
- **persistence:** gestiona la conexión y operaciones con PostgreSQL.
- **launcher:** permite iniciar la aplicación.

## 3. Contrato de estrategias

El módulo `plugin-api` define tres componentes principales:

### Strategy

Interfaz genérica que establece el comportamiento obligatorio de cada algoritmo.

Sus métodos son:

- `id()`: identifica la estrategia.
- `supports(Context)`: determina si puede trabajar con un contexto.
- `execute(I)`: ejecuta el algoritmo y devuelve un resultado.

### Context

Registro inmutable que almacena la información necesaria para evaluar la compatibilidad de una estrategia:

- Origen.
- Destino.
- Criterio de optimización.
- Permiso para utilizar pesos negativos.

### Result

Registro genérico que representa el resultado de una ejecución:

- Estado de éxito.
- Valor devuelto.
- Mensaje descriptivo.
- Tiempo de ejecución.

## 4. Pruebas

Se utiliza JUnit 5 para comprobar el contrato de estrategias.

Las pruebas actuales verifican:

1. Identificación de una estrategia.
2. Aceptación de un contexto compatible.
3. Rechazo de un contexto no compatible.
4. Devolución correcta del resultado de ejecución.

## 5. Estado actual

El módulo `plugin-api` cuenta con su contrato inicial implementado y sus pruebas automatizadas.

Los demás módulos se encuentran pendientes de desarrollo.
