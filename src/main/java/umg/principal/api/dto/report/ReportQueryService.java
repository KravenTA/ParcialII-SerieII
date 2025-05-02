package umg.principal.api.dto.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;

public class ReportQueryService {

    private static final Logger logger = Logger.getLogger(ReportQueryService.class.getName());
    private final EntityManager em;

    public ReportQueryService(EntityManager em) {
        this.em = em;
    }

    public Collection<Report> queryReportsByCountryAndDate(String iso, String dateStr) {
        logger.info("Querying reports for ISO: " + iso + " and date: " + dateStr);

        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);

            TypedQuery<Report> query = em.createQuery(
                    "SELECT r FROM Report r WHERE r.iso = :iso AND r.date = :date",
                    Report.class
            );
            query.setParameter("iso", iso);
            query.setParameter("date", date);

            List<Report> reports = query.getResultList();
            logger.info("Found " + reports.size() + " reports for ISO: " + iso + " on date: " + dateStr);

            TreeMap<String, Report> uniqueReports = new TreeMap<>();

            for (Report report : reports) {
                String key = report.getProvince() != null && !report.getProvince().isEmpty()
                        ? report.getProvince()
                        : report.getName();

                uniqueReports.put(key, report);
            }

            logger.info("=== Report Summary for " + iso + " (" + dateStr + ") ===");
            logger.info(String.format("%-25s | %-8s | %-8s | %-8s", "Province", "Confirmed", "Deaths", "Recovered"));
            logger.info("-------------------------------------------------------------------");

            uniqueReports.forEach((province, report) -> {
                logger.info(String.format("%-25s | %-8d | %-8d | %-8d",
                        province.length() > 25 ? province.substring(0, 22) + "..." : province,
                        report.getConfirmed(),
                        report.getDeaths(),
                        report.getRecovered()));
            });

            logger.info("-------------------------------------------------------------------");
            logger.info("Total unique regions: " + uniqueReports.size());

            return uniqueReports.values();

        } catch (Exception e) {
            logger.severe("❌ Error querying reports: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }


}