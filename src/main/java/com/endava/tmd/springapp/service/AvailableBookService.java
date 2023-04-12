package com.endava.tmd.springapp.service;

import com.endava.tmd.springapp.entity.AvailableBook;
import com.endava.tmd.springapp.entity.Book;
import com.endava.tmd.springapp.entity.RentedBook;
import com.endava.tmd.springapp.entity.User;
import com.endava.tmd.springapp.repository.AvailableBookRepository;
import com.endava.tmd.springapp.repository.BookRepository;
import com.endava.tmd.springapp.repository.RentedBookRepository;
import com.endava.tmd.springapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AvailableBookService {

    @Autowired
    private AvailableBookRepository availableBookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RentedBookRepository rentedBookRepository;

    public AvailableBookService(AvailableBookRepository availableBookRepository){
        this.availableBookRepository = availableBookRepository;
    }

    public List<AvailableBook> getAll(){
        return availableBookRepository.findAll();
    }

    public AvailableBook getById(Long id){
        return availableBookRepository.findById(id).get();
    }

    public void addManualAvailableBook(Long bookId, Long ownerId){
        AvailableBook availableBook = new AvailableBook();
        availableBook.setBook(bookRepository.findById(bookId).get());
        availableBook.setOwner(userRepository.findById(ownerId).get());
        availableBookRepository.saveAndFlush(availableBook);
    }

    public Object addAvailableBook(String bookTitle, String username){

        Book currentBook = bookRepository.findBookByTitle(bookTitle);
        User currentUser = userRepository.findUserByUsername(username);

        List<AvailableBook> currentBooks =  availableBookRepository.getAvailableBooksByBook(currentBook).get();

        for(AvailableBook book : currentBooks){
            if(book.getOwner() == currentUser){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        if(currentBook != null && currentUser != null) {
            AvailableBook availableBook = new AvailableBook();
            availableBook.setBook(currentBook);
            availableBook.setOwner(currentUser);
            availableBookRepository.saveAndFlush(availableBook);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    public void deleteAvailableBook(Long availableBookId){
        availableBookRepository.deleteById(availableBookId);
    }

    public List<AvailableBook> getAllAvailableBooksForCurrentUser(Long id){

        Optional<User> user = userRepository.findById(id);
        User currentUser = user.get();

        List<AvailableBook> booksList = availableBookRepository.getAllAvailableBooksForCurrentUser(currentUser.getUserId());
        List<AvailableBook> availableBooks = new ArrayList<>();

        for(AvailableBook book : booksList){
            if(rentedBookRepository.getRentedBookByBook(book) == null){
                availableBooks.add(book);
            }
        }

        return availableBooks;
    }

    public Object searchBookByAuthorOrTitle(Optional<String> author, Optional<String> title){

        // lista care contine toate cartile din tabelul "books" care au acelasi autor
        List<Book> allMatchingBooks = bookRepository.getBookByAuthorOrTitle(author, title);

        List<AvailableBook> currentAvailableBooks = new ArrayList<>();

        if(allMatchingBooks.size() == 1) {
            currentAvailableBooks = availableBookRepository.getAvailableBooksByBook(allMatchingBooks.get(0)).get();
        }
        else {
            for (Book book: allMatchingBooks){

                // in case of multiple available books with the same name and author but different owners
                List<AvailableBook> availableBookList = availableBookRepository.getAvailableBooksByBook(book).get();

                currentAvailableBooks.addAll(availableBookList);
            }
        }

        if(currentAvailableBooks.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {

            HashMap<String, String> searchedBooks = new HashMap<>();

            for(AvailableBook book : currentAvailableBooks){
                String bookTitle = book.getBook().getTitle();
                String bookOwner = book.getOwner().getUsername();
                String available = "";
                RentedBook currentBook = rentedBookRepository.getRentedBookByBook(book);

                if(currentBook != null) {
                    searchedBooks.put("Book: " + bookTitle + ", Owner: " + bookOwner, available + currentBook.getRentedUntil());
                }
                else {
                    searchedBooks.put("Book: " + bookTitle + ", Owner: " + bookOwner, available + "is available");
                }
            }

            return searchedBooks;
        }
    }

}
