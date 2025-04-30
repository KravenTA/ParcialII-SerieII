package umg.principal.api.main;

import umg.principal.api.service.ExecutorJobHandler;

import java.util.logging.Logger;

public class ApiApplication {

    private static final Logger logger = Logger.getLogger(ApiApplication.class.getName());
    private static final int SEGUNDOS_ESPERA = 15;

    public static void main(String[] args) {
        logger.info("Iniciando aplicación COVID Stats Tracker");

        ExecutorJobHandler executor = new ExecutorJobHandler();
        executor.iniciarProcesamiento(SEGUNDOS_ESPERA);

        logger.info("Aplicación iniciada correctamente. El proceso de consumo de API se ejecutará en " + SEGUNDOS_ESPERA + " segundos");
    }
}
//fin