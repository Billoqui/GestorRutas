package edu.utp.gestorrutas.api;

public interface Strategy<I, O> {
    String id();
    boolean supports(Context contexto);
    Result<O> execute(I entrada);
}