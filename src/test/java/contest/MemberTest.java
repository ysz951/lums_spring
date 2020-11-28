package contest;


import contest.model.Member;
import contest.model.Member.Role;
import org.junit.Test;
import org.junit.Before;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import java.util.*;

import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberTest {
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void capacityTest(){
        String name = "memberOne";
        Role role = Role.ADMIN;
        String email = "mem1@test.com";
        String password = "mempass";
        Member member = createMember(name, role, email, password);
        Member dbMember = (Member) entityManager
                .getEntityManager()
                .createQuery("SELECT m FROM Member m WHERE m.id=:id")
                .setParameter("id", member.getId())
                .getSingleResult();
//        assertThat(member.getName().equals(dbMember.getName())).isTrue();
        assertThat(member.equals(dbMember)).isTrue();
//        assertThat(teamList.equals(dbTeamList)).isTrue();
    }

    private Member createMember(String name, Role role, String email, String password) {
        Member member = new Member();
        member.setName(name);
        member.setRole(role);
        member.setPassword(password);
        member.setEmail(email);
        entityManager.persist(member);
        return member;
    }

}

