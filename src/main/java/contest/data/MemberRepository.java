package contest.data;

import contest.model.Member;
import contest.model.Member.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    LogRepository logReg;

    public void createMember(Member member) {
        em.persist(member);
    }

    public List<Member> getAllMembers() {
        Query query = em.createQuery("SELECT m FROM Member m");
        return (List<Member>) query.getResultList();
    }

    public Member getMemberById(long id) throws Exception {
        Member member = em.find(Member.class, id);
        if (member == null) throw new Exception("error");
        return em.find(Member.class, id);
    }

    public void modifyRole(long userId, Role role, long adminId) throws Exception {

        Member member = getMemberById(userId);
        Role prevRole = member.getRole();
        Member admin = getMemberById(adminId);
        member.setRole(role);
        logReg.logModifyRole(member, admin, prevRole, role);

    }

    public List<Member> findAllMemberByEmail(String email) {
        Query query =
                em.createQuery(
                        "SELECT m from Member m WHERE m.email = :arg1");
        query.setParameter("arg1", email);
        return (List<Member>) query.getResultList();
    }

    public void block(long userId, long adminId) throws Exception {
        Member member = getMemberById(userId);
        Member admin = getMemberById(adminId);
        member.setBlocked(true);
        logReg.logBlockUser(member, admin, true);
    }

    public void unblock(long userId, long adminId) throws Exception {

        Member member = getMemberById(userId);
        Member admin = getMemberById(adminId);
        member.setBlocked(false);
        logReg.logBlockUser(member, admin, false);

    }

    public void updateMember(Member member) {
        em.merge(member);
    }
}
