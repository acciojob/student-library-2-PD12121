package com.driver.Converter;


import com.driver.Dto.AuthorDto;
import com.driver.models.Author;

public class AuthorConverter {

    public static Author convertAuthorToEntity(AuthorDto authorDto){

        Author author =Author.builder().name(authorDto.getName())
                .email(authorDto.getEmail())
                .age(authorDto.getAge())
                .country(authorDto.getCountry()).build();

        return author;

    }


}
