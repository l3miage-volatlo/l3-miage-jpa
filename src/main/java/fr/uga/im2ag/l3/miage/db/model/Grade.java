package fr.uga.im2ag.l3.miage.db.model;

import javax.persistence.*;

// TODO ajouter une named query pour une des requêtes à faire dans le repository
//@Entity
public class Grade {

    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //@OneToOne
    private Subject subject;
    private Float value;
    private Float weight;

    public Long getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public Grade setSubject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public Float getValue() {
        return value;
    }

    public Grade setValue(Float value) {
        this.value = value;
        return this;
    }

    public Float getWeight() {
        return weight;
    }

    public Grade setWeight(Float weight) {
        this.weight = weight;
        return this;
    }
}
