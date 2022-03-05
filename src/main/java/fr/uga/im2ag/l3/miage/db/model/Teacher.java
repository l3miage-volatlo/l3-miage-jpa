package fr.uga.im2ag.l3.miage.db.model;

import javax.persistence.*;
import java.util.List;

// TODO ajouter une named query pour une des requêtes à faire dans le repository
@Entity
@NamedQuery(name=Teacher.FIND_TEACHERS_BY_SUBJECT, query="SELECT p FROM Teacher p WHERE p.teaching.id = :subjectId")
public class Teacher extends Person {

    public static final String FIND_TEACHERS_BY_SUBJECT = "Teacher.findTeachersBySubject";
    @ManyToOne
    private Subject teaching;
    @OneToMany
    private List<Student> favorites;
    @OneToOne
    private GraduationClass heading;

    public Subject getTeaching() {
        return teaching;
    }

    public Teacher setTeaching(Subject teaching) {
        this.teaching = teaching;
        return this;
    }

    public List<Student> getFavorites() {
        return favorites;
    }

    public Teacher setFavorites(List<Student> favorites) {
        this.favorites = favorites;
        return this;
    }

    public GraduationClass getHeading() {
        return heading;
    }

    public Teacher setHeading(GraduationClass heading) {
        this.heading = heading;
        return this;
    }

}
