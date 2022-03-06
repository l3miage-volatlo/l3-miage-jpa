package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TeacherTest extends Base {

    SubjectRepository subjectRepository;
    GraduationClassRepository graduationClassRepository;
    TeacherRepository teacherRepository;

    @BeforeEach
    void before() {
        teacherRepository = daoFactory.newTeacherRepository(entityManager);
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
        graduationClassRepository = daoFactory.newGraduationClassRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveTeacher()  {
        final var subject = Fixtures.createSubject();
        final var graduationClass = Fixtures.createClass();
        final var teacher = Fixtures.createTeacher(subject, graduationClass);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        graduationClassRepository.save(graduationClass);
        teacherRepository.save(teacher);
        entityManager.getTransaction().commit();

        entityManager.detach(teacher);
        entityManager.detach(subject);
        entityManager.detach(graduationClass);

        final var pTeacher = teacherRepository.findById(teacher.getId());
        assertThat(pTeacher).isNotNull().isNotSameAs(teacher);
        assertThat(pTeacher.getFirstName()).isEqualTo(teacher.getFirstName());
    }

    @Test
    void shouldFindHeadingGraduationClassByYearAndName() {
        final var subject = Fixtures.createSubject();
        final var graduationClass = Fixtures.createClass();
        graduationClass.setYear(2022);
        graduationClass.setName("L3 MIAGE");
        final var teacher = Fixtures.createTeacher(subject, graduationClass);

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        graduationClassRepository.save(graduationClass);
        teacherRepository.save(teacher);
        entityManager.getTransaction().commit();

        entityManager.detach(teacher);
        entityManager.detach(subject);
        entityManager.detach(graduationClass);

        final var pTeacher = teacherRepository.findHeadingGraduationClassByYearAndName(2022, "L3 MIAGE");
        assertNotNull(pTeacher);
        assertThat(pTeacher.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(pTeacher.getHeading().getName()).isEqualTo("L3 MIAGE");
        assertThat(pTeacher.getHeading().getYear()).isEqualTo(2022);
    }

}
