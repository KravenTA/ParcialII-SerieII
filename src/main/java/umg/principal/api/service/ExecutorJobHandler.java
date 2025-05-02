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

    public void startProcessing(int secondsDelay) {
        logger.info("Scheduling the API consumption to execute in " + secondsDelay + " seconds");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                processApiData();
                timer.cancel();
            }
        }, secondsDelay * 1000);
    }

    private void processApiData() {
        logger.info("Starting the API consumption process");

        List<String> regions = ApiHttpClient.getRegions();
        logger.info("Retrieved " + regions.size() + " regions");

        LocalDate localDate = ConfigProperties.getReportDate();
        String date = localDate.format(DateTimeFormatter.ISO_DATE);
        String exampleCountryIso = ConfigProperties.getExampleCountry();
        ReportService reportService = new ReportService();

        for (String iso : regions) {
            try {
                logger.info("Processing ISO: " + iso);
                reportService.obtainAndSaveReport(iso, date);
            } catch (Exception e) {
                logger.severe("❌ Error processing ISO: " + iso + " → " + e.getMessage());
            }
        }

        logger.info("=== QUERYING GROUPED DATA FOR " + exampleCountryIso + " ===");
        logger.info("Fetching reports for " + exampleCountryIso + " on date " + date);

        Collection<Report> sortedReports = reportService.getReportsByCountryAndDate(exampleCountryIso, date);
        logger.info("Total records without duplicates: " + sortedReports.size());

        reportService.closeConnection();
        logger.info("API consumption process completed successfully");
    }
}