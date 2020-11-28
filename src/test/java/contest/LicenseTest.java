package contest;


import contest.model.License;
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
public class LicenseTest {
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void capacityTest(){
        int year = 2019;
        double price = 1.23;
        License license = createLicense(year, price);
        License dbLicense = (License) entityManager
                .getEntityManager()
                .createQuery("SELECT l FROM License l WHERE l.id=:id")
                .setParameter("id", license.getId())
                .getSingleResult();
        assertThat(license.equals(dbLicense)).isTrue();
    }

    private License createLicense(int year, double price) {
        License license = new License();
        license.setYear(year);
        license.setPrice(price);
        entityManager.persist(license);
        return license;
    }

}
