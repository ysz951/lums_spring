package contest.data;

import contest.model.License;
import contest.model.Member;
import contest.model.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class LicenseRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private LogRepository logReg;

    @Autowired
    private MemberRepository memberRepo;
    /**
     * Finds a license by its id
     *
     * @param id
     * @return
     */
    public License findById(Long id) {
        return em.find(License.class, id);
    }

    /**
     * Saves a license to the db
     *
     * @param license
     */
    public void save(License license) {
        em.persist(license);
    }

    public List<License> findAllOrderedById() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<License> criteria = cb.createQuery(License.class);
        Root<License> license = criteria.from(License.class);
        criteria.select(license).orderBy(cb.asc(license.get("id")));
        return em.createQuery(criteria).getResultList();
    }

    public void purchase(long licenseId, long userId) throws Exception {
        Member member = memberRepo.getMemberById(userId);
        License license = findById(licenseId);
        Sale sale = new Sale();
        sale.setActive(true);
        sale.setPurchasedDate(LocalDate.now());
        sale.setUser(member);
        member.getSales().add(sale);
        sale.setLicense(license);
        license.getSales().add(sale);
        em.persist(sale);
        logReg.logPurchase(member, license);
    }
}

