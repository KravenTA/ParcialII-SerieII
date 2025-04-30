package umg.principal.api.dto.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import umg.principal.api.service.ApiHttpClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class ReportService {

    private static final Logger logger = Logger.getLogger(ReportService.class.getName());
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public ReportService() {
        this.emf = Persistence.createEntityManagerFactory("COVID");
        this.em = emf.createEntityManager();
    }

    public void obtenerYGuardarReporte(String iso, String dateStr) {
        try {
            logger.info("Obteniendo reporte para ISO: " + iso + " y fecha: " + dateStr);

            // Convertir la cadena de fecha a LocalDate
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);

            // Obtener los reportes desde la API
            List<Report> reports = ApiHttpClient.getReports(iso, dateStr);

            if (reports != null && !reports.isEmpty()) {
                for (Report report : reports) {
                    // Establecer la fecha LocalDate en el objeto Report
                    report.setFecha(date);
                    logger.info("Reporte obtenido correctamente: " + report);

                    // Guardar cada reporte en la base de datos
                    guardarReporte(report);
                }
            } else {
                logger.warning("No se pudo obtener reportes válidos para ISO: " + iso + " y fecha: " + dateStr);
            }

        } catch (IOException e) {
            logger.severe("Error al obtener los reportes de la API: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.severe("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void guardarReporte(Report report) {
        try {
            em.getTransaction().begin();
            em.persist(report);
            em.getTransaction().commit();
            logger.info("✅ Reporte guardado exitosamente: " + report);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.severe("❌ Error al guardar el reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cerrarConexion() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        logger.info("Conexiones cerradas correctamente");
    }
}