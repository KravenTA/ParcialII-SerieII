package umg.principal.api.service;

import umg.principal.api.config.ConfigProperties;
import umg.principal.api.dto.report.Report;
import umg.principal.api.dto.report.ReportService;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class ExecutorJobHandler {

    private static final Logger logger = Logger.getLogger(ExecutorJobHandler.class.getName());

    public void iniciarProcesamiento(int segundosEspera) {
        logger.info("Programando el consumo de la API para ejecutarse en " + segundosEspera + " segundos");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                procesarDatosAPI();
                timer.cancel(); // Detener el timer después de la primera ejecución
            }
        }, segundosEspera * 1000); // Convertir segundos a milisegundos
    }

    private void procesarDatosAPI() {
        logger.info("Iniciando el proceso de consumo de la API");

        List<String> regiones = ApiHttpClient.getRegions(); // ← Obtener todas las regiones
        logger.info("Se obtuvieron " + regiones.size() + " regiones");

        // Obtener la fecha como LocalDate
        LocalDate localDate = ConfigProperties.getReportDate();
        // Convertir la fecha a String en formato ISO
        String date = localDate.format(DateTimeFormatter.ISO_DATE);
        // Utilizar el valor del país ejemplo desde properties
        String isoPaisEjemplo = ConfigProperties.getExampleCountry();
        ReportService reportService = new ReportService();

        for (String iso : regiones) {
            try {
                logger.info("Procesando ISO: " + iso);
                reportService.obtenerYGuardarReporte(iso, date);
            } catch (Exception e) {
                logger.severe("❌ Error procesando ISO: " + iso + " → " + e.getMessage());
            }
        }

        // Aquí agregamos la consulta para mostrar los datos agrupados
        logger.info("=== CONSULTANDO DATOS AGRUPADOS PARA  ==="+isoPaisEjemplo);
        // Puedes elegir un país específico, por ejemplo Guatemala (GTM)

        logger.info("Obteniendo reportes para " + isoPaisEjemplo + " en fecha " + date);
        Collection<Report> reportesOrdenados = reportService.obtenerReportesPorPaisYFecha(isoPaisEjemplo, date);
        logger.info("Total de registros sin duplicados: " + reportesOrdenados.size());

        reportService.cerrarConexion();
        logger.info("Proceso de consumo de API completado exitosamente");
    }
}