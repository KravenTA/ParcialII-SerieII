package umg.principal.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import umg.principal.api.dto.report.Report;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ApiHttpClient {

    private static final Logger logger = Logger.getLogger(ApiHttpClient.class.getName());
    private static final String RAPIDAPI_KEY = "2505eda46amshc60713983b5e807p1da25ajsn36febcbf4a71";
    private static final String RAPIDAPI_HOST = "covid-19-statistics.p.rapidapi.com";

    public static List<String> getRegions() {
        List<String> regions = new ArrayList<>();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://covid-19-statistics.p.rapidapi.com/regions"))
                    .header("X-RapidAPI-Key", RAPIDAPI_KEY)
                    .header("X-RapidAPI-Host", RAPIDAPI_HOST)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode dataNode = rootNode.path("data");
            if (dataNode.isArray()) {
                for (JsonNode node : dataNode) {
                    String iso = node.path("iso").asText();
                    if (!iso.isEmpty()) {
                        regions.add(iso);
                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            logger.severe("Error retrieving regions from the API: " + e.getMessage());
            e.printStackTrace();
        }
        return regions;
    }


    public static List<String> getProvinces(String iso) {
        List<String> provinces = new ArrayList<>();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://covid-19-statistics.p.rapidapi.com/provinces"))
                    .header("X-RapidAPI-Key", RAPIDAPI_KEY)
                    .header("X-RapidAPI-Host", RAPIDAPI_HOST)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"iso\":\"" + iso + "\"}"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode dataNode = rootNode.path("data");
            if (dataNode.isArray()) {
                for (JsonNode node : dataNode) {
                    provinces.add(node.path("name").asText());
                }
            }

        } catch (IOException | InterruptedException e) {
            logger.severe("Error retrieving provinces from the API: " + e.getMessage());
            e.printStackTrace();
        }
        return provinces;
    }

    public static List<Report> getReports(String iso, String date) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://covid-19-statistics.p.rapidapi.com/reports?iso=" + iso + "&date=" + date))
                .header("X-RapidAPI-Key", RAPIDAPI_KEY)
                .header("X-RapidAPI-Host", RAPIDAPI_HOST)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode dataNode = rootNode.path("data");
        List<Report> reports = new ArrayList<>();
        if (dataNode.isArray()) {
            for (JsonNode reportNode : dataNode) {
                Report report = new Report();
                report.setIso(reportNode.path("region").path("iso").asText());
                report.setProvince(reportNode.path("region").path("province").asText());
                report.setName(reportNode.path("region").path("name").asText());
                report.setConfirmed(reportNode.path("confirmed").asInt());
                report.setDeaths(reportNode.path("deaths").asInt());
                report.setRecovered(reportNode.path("recovered").asInt());
                reports.add(report);
            }
        }
        return reports;
    }
}