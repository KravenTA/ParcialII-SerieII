package umg.principal.api.dto.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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

    public boolean fueEjecutadoAnteriormente(String iso, LocalDate fecha) {
        try {
            TypedQuery<ExecutionReport> query = em.createQuery(
                    "SELECT er FROM ExecutionReport er WHERE er.id.countryIso = :iso AND er.id.executionDate = :fecha",
                    ExecutionReport.class
            );
            query.setParameter("iso", iso);
            query.setParameter("fecha", fecha);
            query.getSingleResult();
            return true; // Si no lanza excepción, el reporte ya existe
        } catch (NoResultException e) {
            return false; // No existe el reporte
        }
    }

    public void registrarEjecucion(String iso, LocalDate fecha) {
        try {
            em.getTransaction().begin();
            ExecutionReport report = new ExecutionReport(fecha, iso);
            em.persist(report);
            em.getTransaction().commit();
            logger.info("✅ Ejecución registrada para ISO: " + iso + " y fecha: " + fecha);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.severe("❌ Error al registrar ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }
}