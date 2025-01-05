package com.benjamin.parsy.vtsb.shared.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HttpHeaderName {

    USER_ID("userId");

    private final String name;

}
