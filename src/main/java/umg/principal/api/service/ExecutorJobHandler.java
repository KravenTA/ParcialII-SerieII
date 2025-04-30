package umg.principal.api.service;

import umg.principal.api.dto.report.ReportService;

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

        ReportService reportService = new ReportService();

        for (String iso : regiones) {
            try {
                logger.info("Procesando ISO: " + iso);
                reportService.obtenerYGuardarReporte(iso, date);
            } catch (Exception e) {
                logger.severe("❌ Error procesando ISO: " + iso + " → " + e.getMessage());
            }
        }

        reportService.cerrarConexion();
        logger.info("Proceso de consumo de API completado exitosamente");
    }
}