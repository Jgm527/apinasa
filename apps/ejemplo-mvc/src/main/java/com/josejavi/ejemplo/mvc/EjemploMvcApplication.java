package com.josejavi.ejemplo.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque de la aplicación.
 *
 * {@code @SpringBootApplication} es la anotación mágica que:
 *   1. Marca esta clase como la configuración principal de Spring.
 *   2. Activa el auto-configuración (servidor web, Thymeleaf, Jackson...).
 *   3. Escanea este paquete (y los de debajo) buscando componentes
 *      (@Controller, @Service...).
 *
 * Por eso el controlador está en un subpaquete: com.josejavi.ejemplo.mvc.web
 */
@SpringBootApplication
public class EjemploMvcApplication {

    public static void main(String[] args) {
        // Arranca la aplicación con un servidor web embebido (Tomcat) en el puerto 8080.
        SpringApplication.run(EjemploMvcApplication.class, args);
    }
}
