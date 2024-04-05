package fr.uga.l3miage.spring.tp3.repositories;


import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateRepositoryTest {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private TestCenterRepository testCenterRepository;

    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;



    // IC TESTS DE findAllByTestCenterEntityCode
    @Test

    void testDeBaseFindAllByTestCenterEntityCode(){
        // given
        TestCenterEntity testCenterEntity = TestCenterEntity
                .builder()
                .code(TestCenterCode.PAR)
                .build();


        TestCenterEntity testCenterEntity2 = TestCenterEntity
                .builder()
                .code(TestCenterCode.GRE)
                .build();


        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("test_man_1_firstname")
                .email("test@gmail")
                .testCenterEntity(testCenterEntity)
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .firstname("test_man_2_firstname")
                .email("test2@gmail")
                .testCenterEntity(testCenterEntity2)
                .build();

        testCenterRepository.save(testCenterEntity);
        candidateRepository.save(candidateEntity);

        testCenterRepository.save(testCenterEntity2);
        candidateRepository.save(candidateEntity2);


        //when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.PAR);


        //then
        assertThat(candidateEntitiesResponses).hasSize(1);
        assertThat(candidateEntitiesResponses.stream().findFirst().get().getFirstname()).isEqualTo("test_man_1_firstname");
    }

    @Test
    void testEmptyFindAllByTestCenterEntityCode(){
        // given

        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("test_man_2_firstname")
                .email("test2@gmail")
                .build();

        candidateRepository.save(candidateEntity);

        //when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.PAR);


        //then
        assertThat(candidateEntitiesResponses).hasSize(0);
    }


    //ICI TESTS DE findAllByCandidateEvaluationGridEntitiesGradeLessThan


    // PROBLEME SUR CE TEST, LA FONCTION RENVOIE UN SET DE LONGUEUR ZERO
    @Test
    void testDeBaseFindAllByCandidateEvaluationGridEntitiesGradeLessThan(){

        //given
        CandidateEvaluationGridEntity grid1 = CandidateEvaluationGridEntity
                .builder()
                .grade(2)
                .build();

        CandidateEvaluationGridEntity grid2 = CandidateEvaluationGridEntity
                .builder()
                .grade(15)
                .build();


        CandidateEvaluationGridEntity grid3 = CandidateEvaluationGridEntity
                .builder()
                .grade(18)
                .build();

        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("test_man_1_firstname")
                .email("test@gmail")
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .firstname("test_man_2_firstname")
                .email("test2@gmail")
                .build();


        candidateEvaluationGridRepository.save(grid1);
        candidateEvaluationGridRepository.save(grid2);
        candidateEvaluationGridRepository.save(grid3);

        candidateEntity.setCandidateEvaluationGridEntities(Set.of(grid1));

        candidateEntity2.setCandidateEvaluationGridEntities(Set.of(grid2,grid3));

        candidateRepository.save(candidateEntity);
        candidateRepository.save(candidateEntity2);

        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(5);

        //then
        assertThat(candidateEntitiesResponses).hasSize(1);
        assertThat(candidateEntitiesResponses.stream().findFirst().get().getFirstname()).isEqualTo("test_man_1_firstname");

    }


    @Test
    void testEmptyFindAllByCandidateEvaluationGridEntitiesGradeLessThan(){

        //given


        CandidateEvaluationGridEntity grid2 = CandidateEvaluationGridEntity
                .builder()
                .grade(15)
                .build();


        CandidateEvaluationGridEntity grid3 = CandidateEvaluationGridEntity
                .builder()
                .grade(18)
                .build();

        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("test_man_1_firstname")
                .email("test@gmail")
                .candidateEvaluationGridEntities(Set.of(grid2))
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .firstname("test_man_2_firstname")
                .email("test2@gmail")
                .candidateEvaluationGridEntities(Set.of(grid3))
                .build();

        candidateEvaluationGridRepository.save(grid2);
        candidateEvaluationGridRepository.save(grid3);

        candidateRepository.save(candidateEntity);
        candidateRepository.save(candidateEntity2);


        //when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(5);

        //then
        assertThat(candidateEntitiesResponses).hasSize(0);

    }



    // ICI TESTS DE findAllByHasExtraTimeFalseAndBirthDateBefore

    @Test
    void testDeBaseFindAllByHasExtraTimeFalseAndBirthDateBefore(){

        //given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .hasExtraTime(false)
                .birthDate(LocalDate.parse("2001-01-01"))
                .firstname("test_man_1_firstname")
                .email("test1@gmail")
                .build();


        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .hasExtraTime(true)
                .birthDate(LocalDate.parse("2001-01-01"))
                .firstname("test_man_2_firstname")
                .email("test2@gmail")
                .build();

        CandidateEntity candidateEntity3 = CandidateEntity
                .builder()
                .hasExtraTime(false)
                .birthDate(LocalDate.parse("2005-01-01"))
                .firstname("test_man_3_firstname")
                .email("test3@gmail")
                .build();

        candidateRepository.save(candidateEntity);
        candidateRepository.save(candidateEntity2);
        candidateRepository.save(candidateEntity3);

        //when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("2003-01-01"));

        //then
        assertThat(candidateEntitiesResponses).hasSize(1);
        assertThat(candidateEntitiesResponses.stream().findFirst().get().getFirstname()).isEqualTo("test_man_1_firstname");

    }


    @Test
    void testEmptyFindAllByHasExtraTimeFalseAndBirthDateBefore(){

        //given

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .hasExtraTime(true)
                .birthDate(LocalDate.parse("2001-01-01"))
                .firstname("test_man_2_firstname")
                .email("test2@gmail")
                .build();

        CandidateEntity candidateEntity3 = CandidateEntity
                .builder()
                .hasExtraTime(false)
                .birthDate(LocalDate.parse("2005-01-01"))
                .firstname("test_man_3_firstname")
                .email("test3@gmail")
                .build();

        candidateRepository.save(candidateEntity2);
        candidateRepository.save(candidateEntity3);

        //when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.parse("2003-01-01"));

        //then
        assertThat(candidateEntitiesResponses).hasSize(0);

    }

}


