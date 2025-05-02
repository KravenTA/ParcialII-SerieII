package umg.principal.api.main;

import umg.principal.api.config.ConfigProperties;
import umg.principal.api.service.ExecutorJobHandler;

import java.util.logging.Logger;

public class ApiApplication {

    private static final Logger logger = Logger.getLogger(ApiApplication.class.getName());

    public static void main(String[] args) {
        logger.info("Starting COVID Stats Tracker application");

        int secondsDelay = ConfigProperties.getProcessingDelay();

        ExecutorJobHandler executor = new ExecutorJobHandler();
        executor.startProcessing(secondsDelay);

        logger.info("Application started successfully. The API consumption process will run in " + secondsDelay + " seconds");
    }
}