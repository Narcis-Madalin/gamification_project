package com.endava.tmd.springapp.service;

import com.endava.tmd.springapp.entity.AvailableBook;
import com.endava.tmd.springapp.entity.RentedBook;
import com.endava.tmd.springapp.entity.User;
import com.endava.tmd.springapp.entity.WaitingList;
import com.endava.tmd.springapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitingListService {

    @Autowired
    private WaitingListRepository waitingListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AvailableBookRepository availableBookRepository;

    @Autowired
    private RentedBookRepository rentedBookRepository;

    @Autowired
    private RentedBookService rentedBookService;

    public List<WaitingList> getAllWaitingUsers(){
        return waitingListRepository.findAll();
    }

    public WaitingList getWaitingUserById(Long id){
        return waitingListRepository.findById(id).get();
    }

    public void deleteWaitingUser(Long id){
        waitingListRepository.deleteById(id);
    }

    public void addManualyWaitingUser(Long availableBook_id, Long userId, Long waitingNumber){
        WaitingList waitingList = new WaitingList();
        waitingList.setBook(availableBookRepository.findById(availableBook_id).get());
        waitingList.setWaitingUser(userRepository.findById(userId).get());
        waitingList.setWaitingNumber(waitingNumber);
        waitingListRepository.saveAndFlush(waitingList);
    }

    public Object addWaitingUser(String username, String bookTitle, String owner){

        User currentUser = userRepository.findUserByUsername(username);
        User currentOwner = userRepository.findUserByUsername(owner);
        List<AvailableBook> availableBooksFoundByOwner = availableBookRepository.getAvailableBooksByOwner(currentOwner);

        AvailableBook currentBook = null;

        for (AvailableBook book : availableBooksFoundByOwner){
            if(book.getBook().getTitle().equals(bookTitle)){
                currentBook = book;
            }
        }

        if( currentBook != null &&
            currentUser != null &&
            currentOwner != null &&
            currentUser != currentOwner){

            RentedBook seeIfBookIsRented = rentedBookRepository.getRentedBookByBook(currentBook);

            if(seeIfBookIsRented == null){
                //rentedBookService.addRentedBook(username, bookTitle, "2 weeks", owner);

                return "The book you want to wait for is already available for renting.";
            }

            else if(seeIfBookIsRented.getUser() != currentUser){
                List<WaitingList> seeAllTheWaitingUsersForThisBook = waitingListRepository.getWaitingListByAvailableBook(currentBook);

                Long waitingNumber = 0L;

                for(WaitingList waitingList : seeAllTheWaitingUsersForThisBook){
                    if(waitingList.getWaitingNumber() > waitingNumber){
                        waitingNumber = waitingList.getWaitingNumber();
                    }
                }

                WaitingList newUser = new WaitingList();
                newUser.setBook(currentBook);
                newUser.setWaitingUser(currentUser);
                newUser.setWaitingNumber(waitingNumber + 1L);
                waitingListRepository.saveAndFlush(newUser);

                return new ResponseEntity<>(HttpStatus.OK);
            }

            else{
                return "You already rented this book.";
            }

        }

        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public Object seeWaitingUsersByUsername(String username){

        User currentUser = userRepository.findUserByUsername(username);

        if(currentUser != null){
            return waitingListRepository.getWaitingListByWaitingUser(currentUser);
        }

        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public Object seeWaitingUsersByBook(String bookTitle, String owner){

        List<AvailableBook> availableBookList = availableBookRepository.getAvailableBooksByBook(bookRepository.findBookByTitle(bookTitle)).get();

        AvailableBook currentBook = null;

        for (AvailableBook availableBook : availableBookList){
            if(availableBook.getOwner() == userRepository.findUserByUsername(owner)){
                currentBook = availableBook;
            }
        }

        if(currentBook != null){
            return waitingListRepository.getWaitingListByAvailableBook(currentBook);
        }

        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public Object seeWaitingUsersByWaitingNumber(Long nr){

        return waitingListRepository.getWaitingListByWaitingNumber(nr);
    }

}
