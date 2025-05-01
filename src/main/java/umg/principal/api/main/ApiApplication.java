package umg.principal.api.main;

import umg.principal.api.config.ConfigProperties;
import umg.principal.api.service.ExecutorJobHandler;

import java.util.logging.Logger;

public class ApiApplication {

    private static final Logger logger = Logger.getLogger(ApiApplication.class.getName());

    public static void main(String[] args) {
        logger.info("Iniciando aplicación COVID Stats Tracker");
        logger.info("Cargando propiedades de configuración");
        // Obtener el tiempo de espera desde properties
        int segundosEspera = ConfigProperties.getProcessingDelay();

        ExecutorJobHandler executor = new ExecutorJobHandler();
        executor.iniciarProcesamiento(segundosEspera);

        logger.info("Aplicación iniciada correctamente. El proceso de consumo de API se ejecutará en " + segundosEspera + " segundos");
    }
}
//fin