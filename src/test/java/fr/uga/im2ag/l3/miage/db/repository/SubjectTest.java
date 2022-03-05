package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.StudentRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

class SubjectTest extends Base {

    SubjectRepository subjectRepository;
    TeacherRepository teacherRepository;
    StudentRepository studentRepository;
    GraduationClassRepository graduationClassRepository;

    @BeforeEach
    void before() {
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
        teacherRepository = daoFactory.newTeacherRepository(entityManager);
        studentRepository = daoFactory.newStudentRepository(entityManager);
        graduationClassRepository = daoFactory.newGraduationClassRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveSubject() {

        final var subject = Fixtures.createSubject();

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        entityManager.getTransaction().commit();
        entityManager.detach(subject);

        var pSubject = subjectRepository.findById(subject.getId());
        assertThat(pSubject).isNotNull().isNotSameAs(subject);
        assertThat(pSubject.getName()).isEqualTo(subject.getName());
    }

    @Test
    void shouldGetAllSubject() {
        final var subject = Fixtures.createSubject();
        final var subject2 = Fixtures.createSubject();
        final var subject3 = Fixtures.createSubject();

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        subjectRepository.save(subject2);
        subjectRepository.save(subject3);
        entityManager.getTransaction().commit();

        var subjects = subjectRepository.getAll();

        entityManager.detach(subject);
        entityManager.detach(subject2);
        entityManager.detach(subject3);

        assertThat(subjects).hasSize(3);
        assertThat(subjects).contains(subject, subject2, subject3);
    }

    @Test
    void shouldFindTeachersForSubject() {
        //Generate data
        final var subject = Fixtures.createSubject();
        final var subject1 = Fixtures.createSubject();
        final var graduationClass = Fixtures.createClass();
        final var student1 = Fixtures.createStudent(graduationClass);
        final var student2 = Fixtures.createStudent(graduationClass);
        final var student3 = Fixtures.createStudent(graduationClass);
        final var student4 = Fixtures.createStudent(graduationClass);
        final var teacher1 = Fixtures.createTeacher(subject,graduationClass,student1);
        final var teacher2 = Fixtures.createTeacher(subject,graduationClass,student2);
        final var teacher3 = Fixtures.createTeacher(subject,graduationClass,student3);
        final var teacher4 = Fixtures.createTeacher(subject1,graduationClass,student4);

        //Save data in transaction
        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        subjectRepository.save(subject1);
        graduationClassRepository.save(graduationClass);
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);
        teacherRepository.save(teacher4);
        entityManager.getTransaction().commit();

        // Test the method with data
        final var teachers = subjectRepository.findTeachers(subject.getId());

        //Delete data from previous transaction
        entityManager.detach(subject);
        entityManager.detach(subject1);
        entityManager.detach(graduationClass);
        entityManager.detach(student1);
        entityManager.detach(student2);
        entityManager.detach(student3);
        entityManager.detach(student4);
        entityManager.detach(teacher1);
        entityManager.detach(teacher2);
        entityManager.detach(teacher3);
        entityManager.detach(teacher4);

        //Assert test
        assertThat(teachers).hasSize(3);
        assertThat(teachers).contains(teacher1,teacher2,teacher3);
        assertThat(teachers).doesNotContain(teacher4);
    }

}
