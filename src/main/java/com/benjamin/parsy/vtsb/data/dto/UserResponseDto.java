package com.benjamin.parsy.vtsb.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class UserResponseDto {

    @JsonProperty("totalUsers")
    private final int totalUsers;

    @JsonProperty("names")
    private final Set<String> names;

}
