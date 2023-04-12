package com.endava.tmd.springapp.service;

import com.endava.tmd.springapp.entity.Book;
import com.endava.tmd.springapp.repository.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public List<Book> getAll(){
        return bookRepository.findAll();
    }

    public Book getById(Long id){
        return bookRepository.findById(id).get();
    }

    public Book addBook(Book book){
        return bookRepository.saveAndFlush(book);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

    public Book updateBook(Long id, Book newBook){
        Book currentUser = bookRepository.findById(id).get();
        BeanUtils.copyProperties(newBook, currentUser, "book_id");
        return bookRepository.saveAndFlush(currentUser);
    }
}
