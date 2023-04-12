package com.endava.tmd.springapp.controllers;

import com.endava.tmd.springapp.entity.RentedBook;
import com.endava.tmd.springapp.service.RentedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("rented_books")
public class RentedBookController {

    @Autowired
    private RentedBookService rentedBookService;

    @RequestMapping(method = RequestMethod.GET)
    public List<RentedBook> getAllRentedBooks(){
        return rentedBookService.getAll();
    }

    @RequestMapping(params = "rented_book_id", method = RequestMethod.GET)
    public Object getRentedBook(@RequestParam Long rentedBookId){
        if(rentedBookService.getById(rentedBookId) == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return rentedBookService.getById(rentedBookId);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addManualRentedBook(@RequestParam (name = "available_book_id") Long availableBookId, @RequestParam (name = "id") Long userId, @RequestParam (name = "period") String rentedPeriod){


        rentedBookService.addManualRentedBook(availableBookId, userId, LocalDateTime.now().plusDays(21), rentedPeriod);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteRentedBook(@RequestParam Long rentedBookId){
        rentedBookService.deleteRentedBook(rentedBookId);
    }

    @RequestMapping(value = "/addbook", method = RequestMethod.POST)
    public Object addRentedBook(@RequestParam (name = "id") Long id, @RequestParam(name = "title") String bookTitle, @RequestParam(name = "period") String period, @RequestParam(name = "id_owner") Long id_owner){
        return rentedBookService.addRentedBook(id, bookTitle, period, id_owner);
    }

    @RequestMapping(value = "/borrower", method = RequestMethod.GET)
    public Object seeBorrowerAndRentedUntil(@RequestParam (name = "username") String username){
        return rentedBookService.seeBorrowerAndRentedUntil(username);
    }

    @RequestMapping(value = "/extendRentingPeriod", method = RequestMethod.PATCH)
    public Object extendRentedPeriod(@RequestParam (name = "username") String username, @RequestParam (name = "title") String title, @RequestParam (name = "period") String period){
        return rentedBookService.extendRentedPeriod(username, title, period);
    }

    @RequestMapping(value = "/borrowed", method = RequestMethod.GET)
    public Object seeBorrowedBooks(@RequestParam (name = "username") String username){
        return rentedBookService.seeBorrowedBooks(username);
    }

}
