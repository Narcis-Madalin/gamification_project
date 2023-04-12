package com.endava.tmd.springapp.controllers;

import com.endava.tmd.springapp.entity.Book;
import com.endava.tmd.springapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
public class BooksController {

    @Autowired
    private BookService bookService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Book> getBooks(){
        return bookService.getAll();
    }

    @RequestMapping(params = "book_id", method = RequestMethod.GET)
    public Object getBook(@RequestParam("book_id") Long id){
        if(bookService.getById(id) == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return bookService.getById(id);
        }
    }

    @PostMapping
    public Book addBook(@RequestBody final Book book){
        return bookService.addBook(book);
    }

    @RequestMapping(value = "book_id", method = RequestMethod.DELETE)
    public void deleteUser(@RequestParam Long id){
        bookService.deleteBook(id);
    }

    @RequestMapping(value = "book_id", method = RequestMethod.PUT)
    public Book updateUser(@RequestParam Long id, @RequestBody Book newBook){
        return bookService.updateBook(id, newBook);
    }
}
