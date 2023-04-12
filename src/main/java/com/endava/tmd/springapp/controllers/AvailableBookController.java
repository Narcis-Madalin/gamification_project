package com.endava.tmd.springapp.controllers;

import com.endava.tmd.springapp.entity.AvailableBook;
import com.endava.tmd.springapp.service.AvailableBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("available_books")
public class AvailableBookController {

    @Autowired
    private AvailableBookService availableBookService;

    @RequestMapping( method = RequestMethod.GET)
    public List<AvailableBook> getAvailableBooks(){
        return availableBookService.getAll();
    }

    @RequestMapping(params = "available_book_id", method = RequestMethod.GET)
    public Object getAvailableBook(@RequestParam Long availableBookId){
        if(availableBookService.getById(availableBookId) == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return availableBookService.getById(availableBookId);
        }
    }

    @RequestMapping(value = "/manualAdd", method = RequestMethod.POST)
    public void addManualAvailableBook(@RequestParam (name = "book_id") Long bookId, @RequestParam(name = "owner_id") Long ownerId){
        availableBookService.addManualAvailableBook(bookId, ownerId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Object addAvailableBook(@RequestParam (name = "title") String title, @RequestParam(name = "username") String username){
        return availableBookService.addAvailableBook(title, username);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAvailableBook(@RequestParam Long availableBookId){
        availableBookService.deleteAvailableBook(availableBookId);
    }

    @RequestMapping(value = "/available", method = RequestMethod.GET)
    public List<AvailableBook> getAllAvailableBooksForCurrentUser(@RequestParam(name = "id") Long id){
        return availableBookService.getAllAvailableBooksForCurrentUser(id);
    }

    @RequestMapping(value = "/availability", method = RequestMethod.GET)
    public Object searchBookByAuthorOrTitle(@RequestParam(name = "author") Optional<String> author, @RequestParam(name = "title") Optional<String> title){
        return availableBookService.searchBookByAuthorOrTitle(author, title);
    }
}
