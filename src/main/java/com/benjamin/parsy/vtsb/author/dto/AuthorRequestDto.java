package com.benjamin.parsy.vtsb.author.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorRequestDto {

    @JsonProperty
    @NotEmpty
    private String firstname;

    @JsonProperty
    @NotEmpty
    private String lastname;

}
