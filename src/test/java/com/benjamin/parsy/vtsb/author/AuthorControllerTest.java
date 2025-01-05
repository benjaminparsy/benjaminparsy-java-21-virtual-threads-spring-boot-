package com.benjamin.parsy.vtsb.author;

import com.benjamin.parsy.vtsb.author.dto.AuthorRequestDto;
import com.benjamin.parsy.vtsb.shared.web.HttpHeaderName;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties =
    "spring.threads.virtual.enabled=true"
)
class AuthorControllerTest {

    private static final String BASE_PATH_JSON_EXPECTED = "classpath:response";

    @Autowired
    private MockMvc mockMvc;

    @Sql(scripts = "classpath:data-test.sql")
    @Test
    void getAuthors_AuthorsPresent_ReturnAllAuthors(@Value(BASE_PATH_JSON_EXPECTED + "/getAuthors.json") Resource expectedJson) throws Exception {

        // When and then
        mockMvc.perform(get("/authors")
                        .header(HttpHeaderName.USER_ID.getName(), UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().encoding(StandardCharsets.UTF_8))
                .andExpect(content().json(expectedJson.getContentAsString(StandardCharsets.UTF_8)));

    }

    @Test
    void postAuthors_AuthorOk_SaveAndReturnAuthor() throws Exception {

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
                        .content(json)
                        .header(HttpHeaderName.USER_ID.getName(), UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().encoding(StandardCharsets.UTF_8))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstname").value(firstname))
                .andExpect(jsonPath("$.lastname").value(lastname));

    }

}