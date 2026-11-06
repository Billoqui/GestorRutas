
package edu.utp.gestorrutas.launcher;

import edu.utp.gestorrutas.persistence.DatabaseConnection;
import edu.utp.gestorrutas.persistence.ExecutionRepository;

public class PruebaCrudPostgreSQL {

    public static void main(String[] args) {

        DatabaseConnection conexion = new DatabaseConnection();
        ExecutionRepository repositorio = new ExecutionRepository(conexion);

        System.out.println("================================");
        System.out.println("   PRUEBA CRUD POSTGRESQL");
        System.out.println("================================");

        // 1. CREAR
        System.out.println("\n1. CREAR REGISTRO");

        repositorio.guardar("prueba-crud", 50, true);

        System.out.println("Registro creado correctamente.");

        // 2. CONSULTAR
        System.out.println("\n2. CONSULTAR REGISTRO");

        String registro = repositorio.listar().stream()
                .filter(e -> e.contains("Estrategia: prueba-crud"))
                .reduce((primero, segundo) -> segundo)
                .orElseThrow(() ->
                        new IllegalStateException("No se encontró el registro")
                );

        System.out.println(registro);

        // Obtener el ID del registro creado
        int id = Integer.parseInt(
                registro.substring(
                        registro.indexOf("ID: ") + 4,
                        registro.indexOf(" |")
                )
        );

        // 3. ACTUALIZAR
        System.out.println("\n3. ACTUALIZAR REGISTRO");

        boolean actualizado = repositorio.actualizar(
                id,
                "prueba-crud-actualizada",
                75,
                true
        );

        System.out.println("¿Se actualizó?: " + actualizado);

        repositorio.listar().stream()
                .filter(e -> e.contains("ID: " + id + " |"))
                .forEach(System.out::println);

        // 4. ELIMINAR
        System.out.println("\n4. ELIMINAR REGISTRO");

        boolean eliminado = repositorio.eliminar(id);

        System.out.println("¿Se eliminó?: " + eliminado);

        boolean sigueExistiendo = repositorio.listar().stream()
                .anyMatch(e -> e.contains("ID: " + id + " |"));

        System.out.println("¿Sigue existiendo?: " + sigueExistiendo);

        System.out.println("\n================================");
        System.out.println("       PRUEBA FINALIZADA");
        System.out.println("================================");
    }
}
