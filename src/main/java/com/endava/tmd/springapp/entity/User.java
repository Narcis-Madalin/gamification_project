package com.endava.tmd.springapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "user_id", nullable = false, updatable = false, columnDefinition = "SERIAL")
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Long points;

    // mappedBy = "owner_id",
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<AvailableBook> availableBooksOwner;

    //mappedBy = "user_id",
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RentedBook> rentedBookList;

    @OneToMany(mappedBy = "waitingUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<WaitingList> booksOnWaitingList;

    public User(){

    }

    public List<WaitingList> getBooksOnWaitingList() {
        return booksOnWaitingList;
    }

    public void setBooksOnWaitingList(List<WaitingList> booksOnWaitingList) {
        this.booksOnWaitingList = booksOnWaitingList;
    }

    public Set<AvailableBook> getAvailableBooksOwner() {
        return availableBooksOwner;
    }

    public void setAvailableBooksOwner(Set<AvailableBook> availableBooksOwner) {
        this.availableBooksOwner = availableBooksOwner;
    }

    public List<RentedBook> getRentedBookList() {
        return rentedBookList;
    }

    public void setRentedBookList(List<RentedBook> rentedBookList) {
        this.rentedBookList = rentedBookList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPoints(){ return points;}

    public void setPoints(Long points) { this.points = points;}

    @Override
    public String toString() {
        return "Username + " + getUsername();
    }
}
