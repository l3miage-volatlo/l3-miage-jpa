package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Subject;
import fr.uga.im2ag.l3.miage.db.repository.api.GradeRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

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
        final var grade = Fixtures.createGrade(subject);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        gradeRepository.save(grade);
        entityManager.getTransaction().commit();

        var pGrade = gradeRepository.findById(grade.getId());

        entityManager.detach(grade);
        entityManager.detach(subject);

        assertThat(pGrade).isNotNull();
        assertThat(pGrade.getId()).isEqualTo(grade.getId());
    }

    @Test
    void shouldFailUpgradeGrade() throws InterruptedException {
        //Le test ne passe pas sauf que sa na aucun sens,
        // car dans le schemas de grade l'attribue value et en updatable false...
        final var subject = Fixtures.createSubject();
        final var grade = Fixtures.createGrade(subject);
        grade.setValue(08.75f);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        gradeRepository.save(grade);
        entityManager.flush();
        entityManager.getTransaction().commit();

        entityManager.detach(subject);
        entityManager.detach(grade);

        final var pGrade = gradeRepository.findById(grade.getId());
        pGrade.setValue(18.75f);
        entityManager.getTransaction().begin();
        gradeRepository.save(pGrade);
        entityManager.flush();
        entityManager.getTransaction().commit();

        entityManager.detach(pGrade);

        final var pGrade2 = gradeRepository.findById(grade.getId());
        assertThat(pGrade2.getValue()).isEqualTo(8.75f);
        assertThat(pGrade2.getValue()).isNotEqualTo(pGrade.getValue());
    }

    @Test
    void shouldFindHighestGrades() {
        final var subject = Fixtures.createSubject();
        final var grade1 = Fixtures.createGrade(subject);
        final var grade2 = Fixtures.createGrade(subject);
        final var grade3 = Fixtures.createGrade(subject);
        final var grade4 = Fixtures.createGrade(subject);

        grade1.setValue(10f);
        grade2.setValue(15f);
        grade3.setValue(20f);
        grade4.setValue(25f);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        gradeRepository.save(grade1);
        gradeRepository.save(grade2);
        gradeRepository.save(grade3);
        gradeRepository.save(grade4);
        entityManager.getTransaction().commit();

        entityManager.detach(subject);
        entityManager.detach(grade1);
        entityManager.detach(grade2);
        entityManager.detach(grade3);
        entityManager.detach(grade4);

        final var highestGrades = gradeRepository.findHighestGrades(3);
        assertThat(highestGrades).hasSize(3);
        assertThat(highestGrades.get(0).getValue()).isEqualTo(25f);
        assertThat(highestGrades.get(1).getValue()).isEqualTo(20f);
        assertThat(highestGrades.get(2).getValue()).isEqualTo(15f);
    }

    @Test
    void shouldFindHighestGradesBySubject() {
        final var subject1 = Fixtures.createSubject();
        final var subject2 = Fixtures.createSubject();
        final var grade1 = Fixtures.createGrade(subject2);
        final var grade2 = Fixtures.createGrade(subject1);
        final var grade3 = Fixtures.createGrade(subject2);
        final var grade4 = Fixtures.createGrade(subject1);

        grade1.setValue(10f);
        grade2.setValue(15f);
        grade3.setValue(20f);
        grade4.setValue(25f);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject2);
        subjectRepository.save(subject1);
        gradeRepository.save(grade1);
        gradeRepository.save(grade2);
        gradeRepository.save(grade3);
        gradeRepository.save(grade4);
        entityManager.getTransaction().commit();

        entityManager.detach(subject1);
        entityManager.detach(subject2);
        entityManager.detach(grade1);
        entityManager.detach(grade2);
        entityManager.detach(grade3);
        entityManager.detach(grade4);

        final var highestGrades = gradeRepository.findHighestGradesBySubject(3, subject1);
        assertThat(highestGrades).hasSize(2);
        assertThat(highestGrades.get(0).getValue()).isEqualTo(25f);
        assertThat(highestGrades.get(1).getValue()).isEqualTo(15f);
    }

}
