package com.josejavi.ejemplo.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque de la aplicación.
 *
 * {@code @SpringBootApplication} es la anotación mágica que:
 *   1. Marca esta clase como la configuración principal de Spring.
 *   2. Activa el auto-configuración (Spring monta el servidor web, Jackson, etc.).
 *   3. Escanea este paquete (y los de debajo) buscando componentes (@RestController, @Service...).
 *
 * Por eso el controlador está en un subpaquete: com.josejavi.ejemplo.rest.web
 */
@SpringBootApplication
public class EjemploRestApplication {

    public static void main(String[] args) {
        // Arranca la aplicación con un servidor web embebido (Tomcat) en el puerto 8080.
        SpringApplication.run(EjemploRestApplication.class, args);
    }
}
