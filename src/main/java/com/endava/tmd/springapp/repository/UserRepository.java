package com.endava.tmd.springapp.repository;

import com.endava.tmd.springapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("select u from User u where u.username = :username or u.email = :email")
//    User findUserByUsernameOrEmail(String username, String email);
    User findUserByUsername(String username);



    List<User> findTop5ByOrderByPointsDesc();
}
