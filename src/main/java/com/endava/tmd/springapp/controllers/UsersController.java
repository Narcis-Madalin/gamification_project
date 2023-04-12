package com.endava.tmd.springapp.controllers;

import com.endava.tmd.springapp.entity.Book;
import com.endava.tmd.springapp.entity.User;
import com.endava.tmd.springapp.repository.UserRepository;
import com.endava.tmd.springapp.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UsersController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll(){
        return userService.getAll();
    }

    @RequestMapping(params = "user_id", method = RequestMethod.GET)
    public Object getUser(@RequestParam("user_id") Long id){
        if(userService.getById(id) == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return userService.getById(id);
        }
    }

    @PostMapping
    public User addUser(@RequestBody final User user){
        return userService.addUser(user);
    }

    @RequestMapping(value = "user_id", method = RequestMethod.DELETE)
    public void deleteUser(@RequestParam Long id){
        userService.deleteUser(id);
    }

    @RequestMapping(value = "user_id", method = RequestMethod.PUT)
    public User updateUser(@RequestParam Long id, @RequestBody User newUser){
        return userService.updateUser(id, newUser);
    }

    @RequestMapping(params = "user_id", method = RequestMethod.POST)
    public void addAvailableBook(@RequestParam (name = "user_id") Long userId, @RequestBody Book book){
        userService.addAvailableBook(userId, book);
    }

//    @RequestMapping(value = "/UsernameOrEmail", method = RequestMethod.GET)
//    public User findUserByUsernameOrEmail(@RequestParam (value = "username", required = false) String username, @RequestParam (value = "email", required = false) String email){
//        return userRepository.findUserByUsernameOrEmail(username, email);
//    }

//    @Autowired
//    private UserRepository userRepository;
//
//    @GetMapping
//    public List<User> usersList(){
//        return userRepository.findAll();
//    }
//
//    @GetMapping
//    @RequestMapping("{id}")
//    public User getUser(@PathVariable Long id){
//        return userRepository.findById(id).get();
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public User create(@RequestBody final User user){
//        return userRepository.saveAndFlush(user);
//    }


    @RequestMapping(value = "/leaderboard", method = RequestMethod.GET)
    public Object getPoints(){
        if(userService.getMostPoints() == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return userService.getMostPoints();
        }
    }

}
