package umg.principal.api.service;

import umg.principal.api.dto.report.Report;
import umg.principal.api.dto.report.ReportService;

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

        String date = "2022-04-16"; // Puedes hacerlo dinámico si querés
        String isoPaisEjemplo = "CHN";
        ReportService reportService = new ReportService();

        for (String iso : regiones) {
            try {
                logger.info("Procesando ISO: " + iso);
                reportService.obtenerYGuardarReporte(iso, date);
            } catch (Exception e) {
                logger.severe("❌ Error procesando ISO: " + iso + " → " + e.getMessage());
            }
        }

        // En la clase ExecutorJobHandler.java, en el método iniciarProcesamiento

        // Después del bucle for que procesa todas las regiones pero antes de cerrar la conexión
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

        reportService.cerrarConexion();
        logger.info("Proceso de consumo de API completado exitosamente");
    }
}