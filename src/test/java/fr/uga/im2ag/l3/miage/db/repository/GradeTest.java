package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Subject;
import fr.uga.im2ag.l3.miage.db.repository.api.GradeRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GradeTest extends Base {

    GradeRepository gradeRepository;
    SubjectRepository subjectRepository;

    @BeforeEach
    void before() {
        gradeRepository = daoFactory.newGradeRepository(entityManager);
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveGrade() {
        final var subject = Fixtures.createSubject();
        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        final var grade = Fixtures.createGrade(subject);

        gradeRepository.save(grade);
        entityManager.getTransaction().commit();
        entityManager.detach(grade);
        entityManager.detach(subject);

        var pGrade = gradeRepository.findById(grade.getId());
        assertThat(pGrade).isNotNull();
        assertThat(pGrade.getId()).isEqualTo(grade.getId());
    }

    @Test
    void shouldFailUpgradeGrade() {
        // TODO, ici tester que la mise à jour n'a pas eu lieu.

    }

    @Test
    void shouldFindHighestGrades() {
        // TODO
    }

    @Test
    void shouldFindHighestGradesBySubject() {
        // TODO
    }

}
