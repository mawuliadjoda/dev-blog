package com.esprit.Repostory;

import com.esprit.entity.Author;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Provoque N+1
    // List<Author> findAll();

    // @EntityGraph(attributePaths = "books")

    @QueryHints({
            // @QueryHint(name = org.hibernate.annotations.QueryHints.PASS_DISTINCT_THROUGH, value = "false"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.FETCH_SIZE, value = "50")
    })
    @EntityGraph(attributePaths = {"books", "bio"})
    Page<Author> findAll(Pageable pageable);

    // Évite N+1
    // @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books")
    // List<Author> findAllWithBooks();
}
