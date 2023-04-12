package com.endava.tmd.springapp.service;

import com.endava.tmd.springapp.entity.AvailableBook;
import com.endava.tmd.springapp.entity.Book;
import com.endava.tmd.springapp.entity.User;
import com.endava.tmd.springapp.repository.AvailableBookRepository;
import com.endava.tmd.springapp.repository.BookRepository;
import com.endava.tmd.springapp.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AvailableBookRepository availableBookRepository;

    @Autowired
    private BookService bookService;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getById(Long id){
        return userRepository.findById(id).get();
    }

    public User addUser(User user){
        return userRepository.saveAndFlush(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User newUser){
        User currentUser = userRepository.findById(id).get();
        BeanUtils.copyProperties(newUser, currentUser, "user_id");

        return userRepository.saveAndFlush(currentUser);
    }

    public Boolean verifyUser(String username){
        return userRepository.findUserByUsername(username) != null;
    }


//    public User findUserByUsernameOrEmail(String username, String email){
//        return userRepository.findUserByUsernameOrEmail(username, email);
//    }

    public void addAvailableBook(Long userId, Book book){
        User currentUser = userRepository.findById(userId).get();

        if(bookRepository.findBookByAuthorAndAndTitle(book.getAuthor(), book.getTitle()) == null){
            bookService.addBook(book);
        }

        AvailableBook newAvailableBook = new AvailableBook();
        newAvailableBook.setBook(book);
        newAvailableBook.setOwner(currentUser);
        availableBookRepository.saveAndFlush(newAvailableBook);
    }

    public List<User> getMostPoints(){
        return userRepository.findTop5ByOrderByPointsDesc();
    }


}
