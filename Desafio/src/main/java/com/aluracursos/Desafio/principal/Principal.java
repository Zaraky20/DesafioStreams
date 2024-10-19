package com.aluracursos.Desafio.principal;

import com.aluracursos.Desafio.model.Datos;
import com.aluracursos.Desafio.model.DatosLibros;
import com.aluracursos.Desafio.service.ConsumoAPI;
import com.aluracursos.Desafio.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    public void muestraElMenu() {
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        // Top 10 libros más descargados
        System.out.println("Top 10 libros más descargados");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);

        // Buscar libros por nombre
        System.out.println("Ingresa el nombre del libro que deseas buscar:");
        var tituloLibro = teclado.nextLine();
        // Reemplazamos espacios con '+' para que la búsqueda funcione correctamente en la URL
        json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println("Libro encontrado. ");
            System.out.println(libroBuscado.get());
        } else {
            System.out.println("Libro no encontrado.");
        }
        //Trabajando con estadisticas
        DoubleSummaryStatistics est = datos.resultados().stream()
                .filter(d->d.numeroDeDescargas()>0)
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
        System.out.println("Cantidad media de descragas: " + est.getAverage());
        System.out.println("Cantidad de descargas: " + est.getMax());
        System.out.println("Cantidad minima de descarga: " + est.getMin());
        System.out.println("Cantidad de registros evaluados para calcular las estadisticas:" + est.getCount());



    }
}
