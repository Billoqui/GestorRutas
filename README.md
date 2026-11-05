```powershell
.\mvnw.cmd clean verify
```

En Linux o macOS:

```bash
./mvnw clean verify
```

### Ejecutar la aplicación

En Windows:

```powershell
.\mvnw.cmd -pl launcher exec:java "-Dexec.mainClass=edu.utp.gestorrutas.launcher.GestorRutasApp"
```

También puedes ejecutar `GestorRutasApp` desde IntelliJ IDEA, configurando previamente la variable de entorno `GESTOR_RUTAS_DB_PASSWORD`.

## Pruebas realizadas

Durante el desarrollo se han realizado verificaciones de:

- Compilación de los módulos Maven.
- Pruebas automatizadas.
- Ejecución de estrategias de búsqueda.
- Adaptación automática por tiempo de ejecución.
- Persistencia de ejecuciones y decisiones adaptativas.
- Operaciones CRUD de PostgreSQL.

Se ha comprobado un escenario en el que Dijkstra supera el umbral configurado y el motor cambia automáticamente a Dijkstra implementado en Scala.

También se ha ejecutado `.\mvnw.cmd clean verify` con resultado `BUILD SUCCESS`.

## Estado del desarrollo

- [x] Configuración del entorno de desarrollo.
- [x] Estructura modular Maven.
- [x] API y contrato de estrategias.
- [x] Modelo del dominio y grafos.
- [x] Algoritmos de búsqueda en Java.
- [x] Estrategia adicional en Scala.
- [x] Registro y selección de estrategias.
- [x] Motor de adaptación automática.
- [x] Monitorización de métricas.
- [x] Reglas de adaptación por tiempo y errores.
- [x] Configuración mediante JSON.
- [x] Integración con PostgreSQL.
- [x] Operaciones CRUD.
- [x] Registro de decisiones adaptativas.
- [x] Pruebas de compilación y verificación Maven.
- [ ] Completar y revisar toda la documentación técnica.
- [ ] Verificar todos los entregables de la rúbrica final.

## Repositorio

GitHub: https://github.com/Billoqui/GestorRutas

---

**Proyecto académico individual — Ingeniería de Software**

Desarrollado para aplicar programación orientada a objetos, arquitectura modular, interoperabilidad Java–Scala, selección adaptativa de algoritmos y persistencia de información.