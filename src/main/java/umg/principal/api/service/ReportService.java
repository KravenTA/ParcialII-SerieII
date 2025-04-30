package umg.principal.api.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import umg.principal.api.dto.report.Report;

public class ReportService {

    public static void saveReport(Report report) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("covidPU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(report);
            em.getTransaction().commit();
            System.out.println("[INFO] Report saved to DB.");
        } catch (Exception e) {
            System.err.println("[ERROR] Could not save report: " + e.getMessage());
            em.getTransaction().rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}