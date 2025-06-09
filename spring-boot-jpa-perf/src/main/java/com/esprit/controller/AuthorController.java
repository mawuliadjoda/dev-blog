package com.esprit.controller;


import com.esprit.Repostory.AuthorRepository;
import com.esprit.entity.Author;
import com.esprit.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping
    public void findAll() {

        // List<Author> authors = authorRepository.findAll(); // ou findAllWithBooks()
        List<Author> authors = authorRepository.findAllWithBooks();

        for (Author author : authors) {
            System.out.println("Auteur : " + author.getName());
            for (Book book : author.getBooks()) {
                System.out.println("  Livre : " + book.getTitle());
            }
        }

    }
}
