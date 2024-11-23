package com.esprit.principal.persistence;

import com.esprit.library.persistence.entity.Person;
import com.esprit.library.persistence.repository.PersonRepository;

import java.util.Optional;

public interface PersonCustomRepository extends PersonRepository {
    /**
     * Recherche une personne par email.
     *
     * @param email l'email de la personne
     * @return un Optional contenant la personne si elle est trouvée
     */
    Optional<Person> findByEmail(String email);

    /**
     * Vérifie si une personne existe avec un email donné.
     *
     * @param email l'email à vérifier
     * @return true si une personne existe avec cet email, sinon false
     */
    boolean existsByEmail(String email);
}
