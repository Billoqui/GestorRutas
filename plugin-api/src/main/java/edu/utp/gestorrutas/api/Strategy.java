
package edu.utp.gestorrutas.api;

/**
 * Define el contrato común que deben implementar
 * todas las estrategias de cálculo de rutas.
 *
 * @param <I> tipo de dato que recibe la estrategia
 * @param <O> tipo de dato que devuelve la estrategia
 */
public interface Strategy<I, O> {

    /**
     * Devuelve el identificador único de la estrategia.
     *
     * @return identificador de la estrategia
     */
    String id();

    /**
     * Comprueba si la estrategia puede trabajar
     * con el contexto proporcionado.
     *
     * @param contexto condiciones de ejecución
     * @return true si la estrategia es compatible
     */
    boolean supports(Context contexto);

    /**
     * Ejecuta la estrategia con los datos recibidos.
     *
     * @param entrada datos necesarios para ejecutar el algoritmo
     * @return resultado de la ejecución
     */
    Result<O> execute(I entrada);
}
