package umg.principal.api.dto.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.logging.Logger;

public class ExecutionReportService {
    private static final Logger logger = Logger.getLogger(ExecutionReportService.class.getName());
    private final EntityManager em;

    public ExecutionReportService(EntityManager em) {
        this.em = em;
    }

    public boolean wasExecutedPreviously(String iso, LocalDate date) {
        try {
            TypedQuery<ExecutionReport> query = em.createQuery(
                    "SELECT er FROM ExecutionReport er WHERE er.id.countryIso = :iso AND er.id.executionDate = :date",
                    ExecutionReport.class
            );
            query.setParameter("iso", iso);
            query.setParameter("date", date);
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public void registerExecution(String iso, LocalDate date) {
        try {
            em.getTransaction().begin();
            ExecutionReport report = new ExecutionReport(date, iso);
            em.persist(report);
            em.getTransaction().commit();
            logger.info("✅ Execution registered for ISO: " + iso + " and date: " + date);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.severe("❌ Error registering execution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}