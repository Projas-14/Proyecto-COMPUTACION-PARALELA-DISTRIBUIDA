package server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiExterna {
    private static final String API_KEY = "35d4e193f3b64c449f5dc71c05eb7b80"; // Tu App ID de Open Exchange Rates

    public double obtenerTasaCambioCLPaUSD() {
        String urlString = "https://openexchangerates.org/api/latest.json?app_id=" + API_KEY;
        URL url;
        try {
            url = new URL(urlString); // Crear la URL para la API
        } catch (MalformedURLException e) {
            System.err.println("La URL no es válida: " + e.getMessage());
            return -1; // Devuelve -1 en caso de error
        }

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode(); // Obtener el estado de la respuesta
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    content.append(line); // Construir la respuesta JSON
                }
                in.close();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(content.toString());

                // Obtener el objeto "rates" y el valor para "CLP"
                JsonNode ratesNode = jsonResponse.get("rates");
                double usdToClp = ratesNode.get("CLP").asDouble();

                // Invertir para obtener CLP a USD
                double clpToUsd = 1 / usdToClp; // Para obtener la tasa de CLP a USD

                return clpToUsd; // Devuelve la tasa
            } else {
                System.err.println("Error al conectarse a la API. Código de estado: " + status);
                return -1; // Devuelve -1 en caso de error
            }
        } catch (IOException e) {
            System.err.println("Error al obtener datos de la API: " + e.getMessage());
            return -1; // Devuelve -1 en caso de error
        } finally {
            if (con != null) {
                con.disconnect(); // Cierra la conexión HTTP
            }
        }
    }
}
