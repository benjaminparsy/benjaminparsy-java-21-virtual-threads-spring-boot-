package com.benjamin.parsy.vtsb.author;

import com.benjamin.parsy.vtsb.author.dto.AuthorRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties =
    "activate.virtual.threads=true"
)
class AuthorControllerTest {

    private static final String BASE_PATH_JSON_EXPECTED = "classpath:response";

    @Autowired
    private MockMvc mockMvc;

    @Sql(scripts = "classpath:data-test.sql")
    @Test
    void getBooks_BooksPresent_ReturnAllBooks(@Value(BASE_PATH_JSON_EXPECTED + "/getAuthors.json") Resource expectedJson) throws Exception {

        // When and then
        mockMvc.perform(get("/authors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson.getContentAsString(StandardCharsets.UTF_8)));

    }

    @Sql(scripts = "classpath:data-test.sql")
    @Test
    void postBooks_BookOk_SaveAndReturnBook(@Value(BASE_PATH_JSON_EXPECTED + "/postAuthors.json") Resource expectedJson) throws Exception {

        // Given
        String firstname = "Emile";
        String lastname = "Zola";

        AuthorRequestDto authorRequestDto = AuthorRequestDto.builder()
                .firstname(firstname)
                .lastname(lastname)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writer().writeValueAsString(authorRequestDto);

        // When and then
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().encoding(StandardCharsets.UTF_8))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstname").value(firstname))
                .andExpect(jsonPath("$.lastname").value(lastname));

    }

}