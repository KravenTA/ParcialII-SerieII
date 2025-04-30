package umg.principal.api.service;

import umg.principal.api.dto.province.Province;
import umg.principal.api.dto.region.Region;
import umg.principal.api.dto.report.Report;
import umg.principal.api.dto.report.ReportService;

import java.util.List;

public class CovidApiService {

    public void processCovidData() {
        try {
            // Obtener las regiones y provincias
            List<String> regions = ApiHttpClient.getRegions();
            System.out.println("[INFO] Regions fetched: " + regions.size());

            List<String> provinces = ApiHttpClient.getProvinces("GTM");
            System.out.println("[INFO] Provinces fetched: " + provinces.size());

            // Obtener los reportes para todas las provincias
            List<Report> reports = ApiHttpClient.getReports("GTM", "2022-04-16");
            System.out.println("[INFO] Reports fetched for GTM");

            // Instancia de ReportService
            ReportService reportService = new ReportService();

            // Iterar sobre la lista de reportes y guardar cada uno
            for (Report report : reports) {
                // Guardar el reporte en la base de datos
                reportService.guardarReporte(report);
            }

            // Cierre de conexiones
            reportService.cerrarConexion();

        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
            e.printStackTrace();
        }
    }
}