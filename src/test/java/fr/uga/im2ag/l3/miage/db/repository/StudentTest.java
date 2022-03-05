package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.model.Grade;
import fr.uga.im2ag.l3.miage.db.model.Subject;
import fr.uga.im2ag.l3.miage.db.repository.api.GradeRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.StudentRepository;
import fr.uga.im2ag.l3.miage.db.repository.api.SubjectRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StudentTest extends Base {

    StudentRepository studentRepository;
    GraduationClassRepository graduationClassRepository;
    SubjectRepository subjectRepository;
    GradeRepository gradeRepository;

    @BeforeEach
    void before() {
        studentRepository = daoFactory.newStudentRepository(entityManager);
        graduationClassRepository = daoFactory.newGraduationClassRepository(entityManager);
        subjectRepository = daoFactory.newSubjectRepository(entityManager);
        gradeRepository = daoFactory.newGradeRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            //entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveStudent() {
        final var graduationClass = Fixtures.createClass();
        final var student = Fixtures.createStudent(graduationClass);
        entityManager.getTransaction().begin();
        studentRepository.save(student);
        entityManager.getTransaction().commit();

        var pStudent = studentRepository.findById(student.getId());

        assertThat(pStudent).isNotNull();
        assertThat(pStudent.getFirstName()).isEqualTo(student.getFirstName());
    }

    @Test
    void shouldFindStudentHavingGradeAverageAbove() {
        final var subject = Fixtures.createSubject();
        final var grade1 = Fixtures.createGrade(subject);
        final var grade2 = Fixtures.createGrade(subject);
        final var grade3 = Fixtures.createGrade(subject);
        final var grade4 = Fixtures.createGrade(subject);
        final var grade5 = Fixtures.createGrade(subject);
        final var grade6 = Fixtures.createGrade(subject);
        final var grade7 = Fixtures.createGrade(subject);
        final var grade8 = Fixtures.createGrade(subject);
        final var grade9 = Fixtures.createGrade(subject);
        final var grade10 = Fixtures.createGrade(subject);
        final var grade11 = Fixtures.createGrade(subject);
        final var grade12 = Fixtures.createGrade(subject);
        final var grades = List.of(grade1, grade2, grade3, grade4, grade5, grade6, grade7, grade8, grade9, grade10, grade11, grade12);


        final var graduationClass = Fixtures.createClass();
        final var student1 = Fixtures.createStudent(graduationClass);
        student1.setGrades(List.of(new Grade[]{grade1, grade2, grade3}));
        final var student2 = Fixtures.createStudent(graduationClass);
        student2.setGrades(List.of(new Grade[]{grade4, grade5, grade6}));
        final var student3 = Fixtures.createStudent(graduationClass);
        student3.setGrades(List.of(new Grade[]{grade7, grade8, grade9}));
        final var student4 = Fixtures.createStudent(graduationClass);
        student4.setGrades(List.of(new Grade[]{grade10, grade11, grade12}));

        entityManager.getTransaction().begin();
        subjectRepository.save(subject);
        gradeRepository.save(grade1);
        gradeRepository.save(grade2);
        gradeRepository.save(grade3);
        gradeRepository.save(grade4);
        gradeRepository.save(grade5);
        gradeRepository.save(grade6);
        gradeRepository.save(grade7);
        gradeRepository.save(grade8);
        gradeRepository.save(grade9);
        gradeRepository.save(grade10);
        gradeRepository.save(grade11);
        gradeRepository.save(grade12);
        graduationClassRepository.save(graduationClass);
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        entityManager.getTransaction().commit();


        float minAvg = 8.0f;
        final var students = studentRepository.findStudentHavingGradeAverageAbove(minAvg);
        assertThat(students).isNotNull();
        for (var student : students) {
            final var avg = student.getGrades().stream().reduce(0f,
                    (acc, curentGrade) -> acc + curentGrade.getValue() * curentGrade.getWeight(),Float::sum)
                    / student.getGrades().stream().mapToDouble((value -> value.getWeight())).sum();
            assertThat(avg).isGreaterThanOrEqualTo(minAvg);
        }
    }

}
