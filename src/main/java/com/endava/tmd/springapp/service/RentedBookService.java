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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class RentedBookService {

    @Autowired
    private RentedBookRepository rentedBookRepository;

    @Autowired
    private AvailableBookRepository availableBookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;


    public RentedBookService(RentedBookRepository rentedBookRepository){
        this.rentedBookRepository = rentedBookRepository;
    }

    public List<RentedBook> getAll(){
        return rentedBookRepository.findAll();
    }

    public RentedBook getById(Long id){
        return rentedBookRepository.findById(id).get();
    }

    public void deleteRentedBook(Long id){
        rentedBookRepository.deleteById(id);
    }

    public void addManualRentedBook(Long available_book_id, Long userId, LocalDateTime rentedUntil, String rentedPeriod){
        RentedBook rentedBook = new RentedBook();

        User newUser = new User();

        User currentUser = userRepository.findById(userId).get();


//        User currentUser = user.get();
//        AvailableBook currentBook = null;

        rentedBook.setBook(availableBookRepository.findById(available_book_id).get());
        rentedBook.setUser(userRepository.findById(userId).get());
        rentedBook.setRentedUntil(rentedUntil);
        rentedBook.setRentedPeriod(rentedPeriod);
        rentedBook.setExtendedPeriod(false);

        newUser.setUserId(currentUser.getUserId());
        newUser.setUsername(currentUser.getUsername());
        newUser.setPassword(currentUser.getPassword());
        newUser.setEmail(currentUser.getEmail());
        newUser.setRentedBookList(currentUser.getRentedBookList());
        newUser.setAvailableBooksOwner(currentUser.getAvailableBooksOwner());
        newUser.setBooksOnWaitingList(currentUser.getBooksOnWaitingList());
        newUser.setPoints(currentUser.getPoints() + 10);

//        switch (rentedPeriod) {
//            case "1 week" ->
//                //userRepository.findById(userId).get().setPoints(userRepository.findById(userId).get().getPoints() + 10);
//                    newUser.setPoints(currentUser.getPoints() + 10);
//            case "2 weeks" ->
//                //userRepository.findById(userId).get().setPoints(userRepository.findById(userId).get().getPoints() + 20);
//                    newUser.setPoints(currentUser.getPoints() + 20);
//            case "3 weeks" ->
//                //userRepository.findById(userId).get().setPoints(userRepository.findById(userId).get().getPoints() + 30);
//                    newUser.setPoints(currentUser.getPoints() + 30);
//            case "1 month" ->
//                //userRepository.findById(userId).get().setPoints(userRepository.findById(userId).get().getPoints() + 40);
//                    newUser.setPoints(currentUser.getPoints() + 40);
//        }

        userService.updateUser(userId, newUser);
        rentedBookRepository.saveAndFlush(rentedBook);
    }

    public Object addRentedBook(Long id, String bookTitle, String period, Long id_owner){

        List<AvailableBook> currentBooks = availableBookRepository.getAvailableBooksByBook(bookRepository.findBookByTitle(bookTitle)).get();
        Optional<User> user = userRepository.findById(id);
        User currentUser = user.get();
        AvailableBook currentBook = null;

        for (AvailableBook book : currentBooks){
            if(book.getOwner().getUsername().equals(userRepository.findById(id_owner).get().getUsername())){
                currentBook = book;
            }
        }

        // the case where the book was available for renting from the start
        boolean periodChecker = period.equals("1 week") || period.equals("2 weeks") || period.equals("3 weeks") || period.equals("1 month");
        if(     periodChecker
                && currentBook != null
                && rentedBookRepository.getRentedBookByBook(currentBook) == null
                && currentUser != null
                && currentUser != currentBook.getOwner()) {

            RentedBook rentedBook = new RentedBook();
            rentedBook.setBook(currentBook);
            rentedBook.setUser(currentUser);
            switch (period) {
                case "1 week" -> {
                    rentedBook.setRentedUntil(LocalDateTime.now().plusDays(7));
                    currentUser.setPoints(currentUser.getPoints() + 10);
                }
                case "2 weeks" -> {
                    rentedBook.setRentedUntil(LocalDateTime.now().plusDays(14));
                    currentUser.setPoints(currentUser.getPoints() + 20);
                }

                case "3 weeks" -> {
                    rentedBook.setRentedUntil(LocalDateTime.now().plusDays(21));
                    currentUser.setPoints(currentUser.getPoints() + 30);
                }
                case "1 month" -> {
                    rentedBook.setRentedUntil(LocalDateTime.now().plusMonths(1));
                    currentUser.setPoints(currentUser.getPoints() + 40);
                }
            }
            rentedBook.setRentedPeriod(period);
            rentedBook.setExtendedPeriod(false);
            rentedBookRepository.saveAndFlush(rentedBook);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        // the case where the book is already rented

        else if(        periodChecker
                        && currentBook != null
                        && rentedBookRepository.getRentedBookByBook(currentBook) != null
                        && currentUser != null
                        && currentUser != currentBook.getOwner()
        ){
            return "This book is already rented. If you want, you can be added to the waiting list for when it becomes available again.";
        }

        // the case where the requested information is wrong(or doesn't exist)
        else{
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    public Object seeBorrowerAndRentedUntil(String username){
        HashMap<String, LocalDateTime> borrowerAndRentedUntil = new HashMap<>(); // pastreaza userul care a imprumutat cartea si cand trebuie sa o returneze

        //HashMap<String, HashMap<String, LocalDateTime>> booksAndBorrower = new HashMap<>(); // pastreaza numele cartii imprumutate + userul si cand trebuie sa o returneze

        User owner = userRepository.findUserByUsername(username);

        if(owner != null) {
            List<AvailableBook> booksOfTheOwner = availableBookRepository.getAvailableBooksByOwner(owner);

            List<RentedBook> rentedBooksOfTheOwner = new ArrayList<>();

            List<RentedBook> rentedBookList;

            for (AvailableBook book : booksOfTheOwner) {
                if (rentedBookRepository.getRentedBooksByBook(book) != null) {
                    //rentedBooksOfTheOwner.add(rentedBookRepository.getRentedBookByBook(book));
                    rentedBookList = rentedBookRepository.getRentedBooksByBook(book);
                    for (RentedBook rentedBook : rentedBookList) {
                        if (rentedBook.getBook().getOwner() == owner) {
                            rentedBooksOfTheOwner.add(rentedBook);
                        }
                    }
                }
            }

            for (RentedBook rentedBook : rentedBooksOfTheOwner) {
                borrowerAndRentedUntil.put(rentedBook.getUser().getUsername(), rentedBook.getRentedUntil());
            }


            return borrowerAndRentedUntil;
        }

        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    public Object extendRentedPeriod(String username, String bookTitle, String period){

        User currentUser = userRepository.findUserByUsername(username);
        Book currentBook = bookRepository.findBookByTitle(bookTitle);

        List<RentedBook> rentedBooks = rentedBookRepository.getRentedBooksByUser(currentUser);

        if(rentedBooks != null && currentUser != null && (period.equals("1 week") || period.equals("2 weeks")) && currentBook != null){

            for(RentedBook book : rentedBooks){

                if(book.getBook().getBook().getTitle().equals(bookTitle) && !book.getExtendedPeriod()){
                    switch (period){
                        case "1 week" -> {book.setRentedUntil(book.getRentedUntil().plusDays(7));
                            book.setRentedPeriod(period);}
                        case "2 weeks" -> {
                            book.setRentedUntil(book.getRentedUntil().plusDays(14));
                            book.setRentedPeriod(period);
                        }
                    }

                    book.setExtendedPeriod(true);
                    rentedBookRepository.save(book);
                    return new ResponseEntity<>(HttpStatus.OK);
                }

                else return "The period was already extended for this book by " + book.getRentedPeriod() + ". You can't extend the period more than 1 time.";
            }

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public Object seeBorrowedBooks(String username){

        HashMap<String, LocalDateTime> borrowedBooks = new HashMap<>();

        User user = userRepository.findUserByUsername(username);

        if(user != null){
            List<RentedBook> borrowedBooksList = rentedBookRepository.getRentedBooksByUser(user);

            for (RentedBook book : borrowedBooksList){
                borrowedBooks.put(book.getBook().getBook().getTitle(), book.getRentedUntil());
            }

            return borrowedBooks;
        }

        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
