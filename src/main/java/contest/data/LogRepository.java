package contest.data;

import contest.model.License;
import contest.model.Log;
import contest.model.Member;
import contest.model.Member.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class LogRepository {

    @PersistenceContext
    private EntityManager em;

    public Log findById(long id) {
        return em.find(Log.class, id);
    }

    public List<Log> findAllOrderedByTime() {
        Query query = em.createQuery("SELECT l from Log l ORDER BY l.time DESC");
        return (List<Log>) query.getResultList();
    }

    public List<Log> findAllLogByUserIdLatestOrder(long userId) {
        Query query =
                em.createQuery("SELECT l from Log l WHERE l.user.id = :arg1 ORDER BY l.time DESC");
        query.setParameter("arg1", userId);
        return (List<Log>) query.getResultList();
    }

    public List<Log> findAllLogByUserIdAndLicenseId(long userId, long licenseId) {
        Query query =
                em.createQuery(
                        "SELECT l from Log l WHERE l.user.id = :arg1 AND l.license.id = :arg2"
                                + " ORDER BY l.time DESC");
        query.setParameter("arg1", userId);
        query.setParameter("arg2", licenseId);
        return (List<Log>) query.getResultList();
    }


    public void register(Log log)  {
        log.setTimeCurrent();
        em.persist(log);
    }

    public void logBlockUser(Member member, Member admin, boolean isBlocked) {
        Log log = new Log();
        log.setUser(member);
        log.setAdmin(admin);
        if (isBlocked) {
            log.setLog("Blocked");
        } else {
            log.setLog("Unblocked");
        }

        register(log);
    }

    public void logPurchase(Member member, License license) {
        Log log = new Log();
        log.setUser(member);
        log.setLicense(license);
        register(log);
    }

    public void logModifyRole(Member member, Member admin, Role prevRole, Role newRole)
            throws Exception {
        Log log = new Log();
        log.setUser(member);
        log.setAdmin(admin);
        log.setPrevRole(prevRole);
        log.setNewRole(newRole);

        register(log);
    }
}

