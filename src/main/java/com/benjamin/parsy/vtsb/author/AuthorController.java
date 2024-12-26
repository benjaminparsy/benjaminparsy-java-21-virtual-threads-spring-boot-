package com.benjamin.parsy.vtsb.author;

import com.benjamin.parsy.vtsb.author.dto.AuthorRequestDto;
import com.benjamin.parsy.vtsb.author.dto.AuthorResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/authors")
@Slf4j
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    public AuthorController(AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getAuthors() {

        log.info("Call to GET /authors");
        log.info("Current thread : {}", Thread.currentThread());

        List<Author> authorList = new LinkedList<>(authorService.findAll());

        return ResponseEntity.ok(authorMapper.toDtoList(authorList));
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDto> postAuthors(@RequestBody @NotNull @Valid AuthorRequestDto authorRequestDto) {

        log.info("Call to POST /authors");
        log.info("Current thread : {}", Thread.currentThread());

        Author author = authorService.save(authorMapper.toEntity(authorRequestDto));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorMapper.toDto(author));
    }

}
