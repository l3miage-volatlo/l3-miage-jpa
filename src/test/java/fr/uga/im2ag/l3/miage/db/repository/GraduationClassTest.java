package fr.uga.im2ag.l3.miage.db.repository;

import fr.uga.im2ag.l3.miage.db.repository.api.GraduationClassRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GraduationClassTest extends Base {

    GraduationClassRepository classRepository;

    @BeforeEach
    void before() {
        classRepository = daoFactory.newGraduationClassRepository(entityManager);
    }

    @AfterEach
    void after() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    @Test
    void shouldSaveClass() {
        final var graduationClass = Fixtures.createClass();
        entityManager.getTransaction().begin();
        classRepository.save(graduationClass);
        entityManager.getTransaction().commit();

        entityManager.detach(graduationClass);

        final var savedClass = classRepository.findById(graduationClass.getId());
        assertNotNull(savedClass);
        assertEquals(graduationClass.getName(), savedClass.getName());
    }


    @Test
    void shouldFindByYearAndName() {
        final var graduationClass = Fixtures.createClass();
        entityManager.getTransaction().begin();
        classRepository.save(graduationClass);
        entityManager.getTransaction().commit();

        entityManager.detach(graduationClass);

        final var foundClass = classRepository.findByYearAndName(graduationClass.getYear(), graduationClass.getName());
        assertNotNull(foundClass);
        assertEquals(graduationClass.getName(), foundClass.getName());
    }

}
