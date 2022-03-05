package fr.uga.im2ag.l3.miage.db.repository.impl;

import fr.uga.im2ag.l3.miage.db.repository.api.StudentRepository;
import fr.uga.im2ag.l3.miage.db.model.Student;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl extends BaseRepositoryImpl implements StudentRepository {


    /**
     * Build a base repository
     *
     * @param entityManager the entity manager
     */
    public StudentRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void save(Student entity) {
        entityManager.persist(entity);

    }

    @Override
    public void delete(Student entity) {
        entityManager.remove(entity);

    }

    @Override
    public Student findById(Long id) {
        return entityManager.find(Student.class, id);
    }

    @Override
    public List<Student> getAll() {
        return entityManager.createQuery("SELECT s FROM Student s", Student.class).getResultList();
    }

    @Override
    public List<Student> findStudentHavingGradeAverageAbove(float minAverage) {
        final List<Student> students = new ArrayList<>();
        final var result= entityManager.createNativeQuery("" +
                        "SELECT * , (sum(g.grade_value*g.weight)/sum(g.weight)) FROM Person p " +
                        "inner join Person_Grade pg on p.id = pg.Student_id and p.DTYPE='Student'" +
                        "inner join Grade g on pg.grades_id = g.id " +
                        "where g.weight>0 "+
                        "group by p.id,pg.grades_id "+
                        "having (sum(g.grade_value*g.weight)/sum(g.weight))>?1 ;"
                        , Object.class).setParameter(1,minAverage);
        final var studentsList = result.getResultList();
        for (var student : studentsList) {
            if(student instanceof Student){
                students.add((Student) student);
            }
        }
        return students;
    }
}
