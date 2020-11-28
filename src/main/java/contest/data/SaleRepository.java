package contest.data;

import contest.model.Sale;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class SaleRepository {

    @PersistenceContext
    private EntityManager em;

    public Sale findById(long id) {
        return em.find(Sale.class, id);
    }

    public List<Sale> findAllOrderedByTime() {
        Query query = em.createQuery("SELECT s from Sale s ORDER BY s.purchasedDate DESC");
        return (List<Sale>) query.getResultList();
    }

    public List<Sale> findAllSaleByUserIdLatestOrder(long userId) {
        Query query =
                em.createQuery(
                        "SELECT s from Sale s WHERE s.user.id = :arg1 ORDER BY s.purchasedDate"
                                + " DESC");
        query.setParameter("arg1", userId);
        return (List<Sale>) query.getResultList();
    }

    public List<Sale> findAllSaleByUserIdAndLicenseId(long userId, long licenseId) {
        Query query =
                em.createQuery(
                        "SELECT s from Sale s WHERE s.user.id = :arg1 AND s.license.id = :arg2"
                                + " ORDER BY s.purchasedDate DESC");
        query.setParameter("arg1", userId);
        query.setParameter("arg2", licenseId);
        return (List<Sale>) query.getResultList();
    }
}

