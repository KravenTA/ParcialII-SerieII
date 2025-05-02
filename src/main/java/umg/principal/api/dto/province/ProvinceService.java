package umg.principal.api.dto.province;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Persistence;
import umg.principal.api.service.ApiHttpClient;

import java.util.List;
import java.util.logging.Logger;

public class ProvinceService {

    private static final Logger logger = Logger.getLogger(ProvinceService.class.getName());
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public ProvinceService() {
        this.emf = Persistence.createEntityManagerFactory("COVID");
        this.em = emf.createEntityManager();
    }

    public ProvinceService(EntityManager em) {
        this.emf = null;
        this.em = em;
    }

    public void obtainAndSaveProvinces(String iso) {
        try {
            logger.info("Fetching provinces for ISO: " + iso);

            List<Province> provinces = ApiHttpClient.getProvinceDetails(iso);

            if (provinces != null && !provinces.isEmpty()) {
                for (Province province : provinces) {
                    // Verificar si la provincia ya existe antes de guardarla
                    if (!provinceExists(iso, province.getProvince())) {
                        saveProvince(province);
                    } else {
                        logger.info("⏭️ Province: " + province.getProvince() + " for ISO: " + iso + " already exists in the database");
                    }
                }
                logger.info("✅ Provinces for ISO: " + iso + " processed and saved successfully (" + provinces.size() + " provinces)");
            } else {
                logger.warning("⚠️ No province data was obtained for ISO: " + iso);
            }
        } catch (Exception e) {
            logger.severe("❌ Error processing provinces for ISO: " + iso + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean provinceExists(String iso, String provinceName) {
        try {
            TypedQuery<Province> query = em.createQuery(
                    "SELECT p FROM Province p WHERE p.iso = :iso AND p.province = :province",
                    Province.class
            );
            query.setParameter("iso", iso);
            query.setParameter("province", provinceName);
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    private void saveProvince(Province province) {
        try {
            em.getTransaction().begin();
            em.persist(province);
            em.getTransaction().commit();
            logger.info("✅ Province saved: " + province.getProvince() + " for " + province.getName());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.severe("❌ Error saving the province: " + e.getMessage());
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
