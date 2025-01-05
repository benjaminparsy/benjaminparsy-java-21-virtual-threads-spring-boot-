package com.benjamin.parsy.vtsb.handler;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponseRecord(@JsonProperty int code, @JsonProperty String description) {
}
