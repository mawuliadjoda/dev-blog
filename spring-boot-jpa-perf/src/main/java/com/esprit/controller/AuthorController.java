package com.esprit.controller;


import com.esprit.Repostory.AuthorRepository;
import com.esprit.entity.Author;
import com.esprit.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    //@GetMapping
    public void findAll() {

         List<Author> authors = authorRepository.findAll(); // ou findAllWithBooks()
        //List<Author> authors = authorRepository.findAllWithBooks();

        for (Author author : authors) {
            System.out.println("Auteur : " + author.getName());
            for (Book book : author.getBooks()) {
                System.out.println("  Livre : " + book.getTitle());
            }
        }

    }

    @GetMapping
    public Page<AuthorDto> getAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Author> authorPage = authorRepository.findAll(pageable);

        return authorPage.map(AuthorDto::fromEntity);
    }


    public record BookDto(Long id, String title) {}

    public record AuthorDto(Long id, String name, List<BookDto> books) {
        public static AuthorDto fromEntity(Author author) {
            List<BookDto> books = author.getBooks().stream()
                    .map(b -> new BookDto(b.getId(), b.getTitle()))
                    .toList();
            return new AuthorDto(author.getId(), author.getName(), books);
        }
    }

}
