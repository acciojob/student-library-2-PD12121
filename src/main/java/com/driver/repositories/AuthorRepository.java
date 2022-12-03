package com.driver.repositories;

import com.driver.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;



public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
