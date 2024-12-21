package com.benjamin.parsy.vtsb.author.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorResponseDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String firstname;

    @JsonProperty
    private String lastname;

}
