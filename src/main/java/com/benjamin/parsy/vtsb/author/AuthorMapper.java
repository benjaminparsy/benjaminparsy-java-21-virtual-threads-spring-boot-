package com.benjamin.parsy.vtsb.author;

import com.benjamin.parsy.vtsb.author.dto.AuthorRequestDto;
import com.benjamin.parsy.vtsb.author.dto.AuthorResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorResponseDto toDto(Author author);

    List<AuthorResponseDto> toDtoList(List<Author> authorList);

    Author toEntity(AuthorRequestDto authorRequestDto);

}
