package com.esprit.principal;


import com.esprit.library.persistence.entity.Person;
import com.esprit.library.persistence.repository.PersonRepository;
import com.esprit.principal.persistence.PersonCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Log4j2
@RequiredArgsConstructor
@Component
public class PersonPopulator implements CommandLineRunner {

    // private final PersonRepository repository;



    private final PersonCustomRepository repository;

    @Override
    public void run(String... args) throws Exception {
        Person person = Person.builder()

                .email("koffimawuli.adjoda@gmail.com")
                .lastName("ADJODA")
                .firstName("Mawuli")
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(person);

        log.info("existsByEmail: " + repository.existsByEmail(person.getEmail()));
    }
}
