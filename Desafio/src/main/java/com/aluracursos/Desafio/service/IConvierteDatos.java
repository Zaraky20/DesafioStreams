package com.aluracursos.Desafio.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
