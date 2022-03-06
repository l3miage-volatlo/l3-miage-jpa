package fr.uga.im2ag.l3.miage.db.model;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

// TODO ajouter une named query pour une des requêtes à faire dans le repository
@Entity
public class Student extends Person {

    //Permet de ne pas avoir à save la class avant de save la person
    @ManyToOne(cascade = CascadeType.PERSIST)
    private GraduationClass belongTo;
    //Permet de ne pas avoir à save les notes avant de save la person
    @OneToMany(cascade = CascadeType.PERSIST)
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
