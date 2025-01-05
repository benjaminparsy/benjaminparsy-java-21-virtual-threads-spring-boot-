package com.benjamin.parsy.vtsb.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class DetailResponseDto {

    @JsonProperty("totalCalls")
    private final int totalCalls;

    @JsonProperty("details")
    private final Set<Detail> details;

    @RequiredArgsConstructor
    public static class Detail {

        @JsonProperty("username")
        private final String username;

        @JsonProperty("endpoints")
        private final Set<Endpoint> endpoints;

        @RequiredArgsConstructor
        public static class Endpoint {

            @JsonProperty("name")
            private final String name;

            @JsonProperty("count")
            private final int count;

        }

    }

}