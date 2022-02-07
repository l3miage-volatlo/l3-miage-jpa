package fr.uga.im2ag.l3.miage.db.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

// TODO ajouter une named query pour une des requêtes à faire dans le repository
//@Entity
public class Student extends Person {

    //@ManyToOne
    private GraduationClass belongTo;
    //@OneToMany
    private List<Grade> grades;

    public GraduationClass getBelongTo() {
        return belongTo;
    }

    public Student setBelongTo(GraduationClass belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public Student setGrades(List<Grade> grades) {
        this.grades = grades;
        return this;
    }
}
