package com.driver.Dto;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDto {

    String name;
    String email;
    int age;
    String country;

}
