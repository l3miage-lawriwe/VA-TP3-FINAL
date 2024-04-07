package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCenterRepository  extends JpaRepository<TestCenterEntity,Long> {

    TestCenterEntity findByCodeEquals(TestCenterCode code);

}
