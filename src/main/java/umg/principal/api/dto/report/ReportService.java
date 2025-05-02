package umg.principal.api.dto.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import umg.principal.api.service.ApiHttpClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class ReportService {

    private static final Logger logger = Logger.getLogger(ReportService.class.getName());
    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final ExecutionReportService executionReportService;

    public ReportService() {
        this.emf = Persistence.createEntityManagerFactory("COVID");
        this.em = emf.createEntityManager();
        this.executionReportService = new ExecutionReportService(em);
    }

    public void obtainAndSaveReport(String iso, String dateStr) {
        try {
            logger.info("Processing report for ISO: " + iso + " and date: " + dateStr);

            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);

            if (executionReportService.wasExecutedPreviously(iso, date)) {
                logger.info("⏭️ ISO: " + iso + " skipped - already processed previously for the date: " + dateStr);
                return;
            }

            List<Report> reports = ApiHttpClient.getReports(iso, dateStr);

            if (reports != null && !reports.isEmpty()) {
                for (Report report : reports) {
                    report.setFecha(date);
                    saveReport(report);
                }

                executionReportService.registerExecution(iso, date);
                logger.info("✅ ISO: " + iso + " processed and saved successfully for the date: " + dateStr);
            } else {
                logger.warning("⚠️ No data was obtained for ISO: " + iso + " and date: " + dateStr);

                executionReportService.registerExecution(iso, date);
                logger.info("📝 Execution was recorded for ISO: " + iso + " even though no data was found");
            }

        } catch (Exception e) {
            logger.severe("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Collection<Report> getReportsByCountryAndDate(String iso, String dateStr) {
        ReportQueryService queryService = new ReportQueryService(em);
        return queryService.queryReportsByCountryAndDate(iso, dateStr);
    }

    public void saveReport(Report report) {
        try {
            em.getTransaction().begin();
            em.persist(report);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.severe("❌ Error saving the report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        logger.info("Connections closed successfully");
    }
}