package umg.principal.api.dto.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ReportDao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("COVID");

    public void guardarReporte(Report reporte) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(reporte);
            em.getTransaction().commit();
            System.out.println("✅ Reporte guardado exitosamente: " + reporte);
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("❌ Error al guardar el reporte: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void cerrar() {
        emf.close();
    }
}