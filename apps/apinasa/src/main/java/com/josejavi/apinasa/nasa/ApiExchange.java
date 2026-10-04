package com.josejavi.apinasa.nasa;

/** Intercambio observado durante la petición actual, además de los datos ya parseados. */
public record ApiExchange<T>(
    String method,
    String requestUrl,
    int statusCode,
    String contentType,
    String responseBody,
    T data,
    String error
) {
    public boolean successful() {
        return statusCode >= 200 && statusCode < 300 && error == null;
    }

    public String statusLabel() {
        return statusCode == 0 ? "Sin respuesta HTTP" : "HTTP " + statusCode;
    }
}