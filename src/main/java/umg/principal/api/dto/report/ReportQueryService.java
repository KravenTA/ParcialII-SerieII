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

    public Collection<Report> consultarReportesPorPaisYFecha(String iso, String fechaStr) {
        logger.info("Consultando reportes para ISO: " + iso + " y fecha: " + fechaStr);

        try {
            // Convertir la cadena de fecha a LocalDate
            LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_DATE);

            // Consultar todos los reportes para el país y fecha especificados
            TypedQuery<Report> query = em.createQuery(
                    "SELECT r FROM Report r WHERE r.iso = :iso AND r.fecha = :fecha",
                    Report.class
            );
            query.setParameter("iso", iso);
            query.setParameter("fecha", fecha);

            List<Report> reportes = query.getResultList();
            logger.info("Se encontraron " + reportes.size() + " reportes para ISO: " + iso + " en la fecha: " + fechaStr);

            // Usar TreeMap para eliminar duplicados y ordenar alfabéticamente por provincia
            TreeMap<String, Report> reportesUnicos = new TreeMap<>();

            for (Report reporte : reportes) {
                // Usar provincia como clave única o, si no está disponible, usar otro identificador
                String clave = reporte.getProvince() != null && !reporte.getProvince().isEmpty()
                        ? reporte.getProvince()
                        : reporte.getName();

                reportesUnicos.put(clave, reporte);
            }

            // Modificación para ReportQueryService.java
            // Reemplaza el bloque de código que muestra los reportes en la consola

            // Mostrar los reportes ordenados y sin duplicados
            logger.info("Reportes agrupados por provincia para ISO: " + iso + ":");
            reportesUnicos.forEach((provincia, reporte) ->
                    logger.info("Report{" +
                            "id=" + reporte.getId() +
                            ", iso='" + reporte.getIso() + '\'' +
                            ", province='" + provincia + '\'' +
                            ", fecha=" + reporte.getFecha() +
                            ", confirmed=" + reporte.getConfirmed() +
                            ", deaths=" + reporte.getDeaths() +
                            ", recovered=" + reporte.getRecovered() +
                            ", name='" + reporte.getName() + '\'' +
                            '}'));

            return reportesUnicos.values();

        } catch (Exception e) {
            logger.severe("❌ Error al consultar reportes: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Retornar lista vacía en caso de error
        }
    }
}
