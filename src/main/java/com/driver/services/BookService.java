package com.driver.services;

import com.driver.models.Author;
import com.driver.models.Book;
import com.driver.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {



    @Autowired
    BookRepository bookRepository2;

    public void createBook(Book book){
        Author author = book.getAuthor();
        if(author!= null){
            if(author.getBooksWritten() == null){
                author.setBooksWritten(new ArrayList<>());
            }
            author.getBooksWritten().add(book);
        }
        bookRepository2.save(book);
    }

    public List<Book> getBooks(String genre, boolean available, String author){
        List<Book> books =null;  //find the elements of the list by yourself

//        if(author == null && available == true){
//            books = bookRepository2.findBooksByGenre(genre,available);
//            return books;
//        } else if (author == null && genre!=null && available == true) {
//            books = bookRepository2.findBooksByGenre(genre,available);
//            return books;
//        } else if (genre == null) {
//            books = bookRepository2.findBooksByAuthor(author,available);
//            return books;
//        } else if(available == false){
//            books = bookRepository2.findBooksByGenreAuthor(genre,author,available);
//            return books;
//        }
        return books;
    }
}
