package com.endava.tmd.springapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "available_books")
public class AvailableBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "available_book_id", nullable = false, updatable = false, columnDefinition = "SERIAL")
    private Long availableBookId;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    Book book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    User owner;

    // book from RentedBook(available_book_id)
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RentedBook> availableRentedBookList;

    // book from WaitingList(available_book_id)
    @OneToMany(mappedBy = "availableBook", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<WaitingList> booksOnTheWaitingList;



    public AvailableBook(){

    }

    public List<RentedBook> getAvailableRentedBookList() {
        return availableRentedBookList;
    }

    public void setAvailableRentedBookList(List<RentedBook> availableRentedBookList) {
        this.availableRentedBookList = availableRentedBookList;
    }

    public Long getAvailableBookId() {
        return availableBookId;
    }

    public void setAvailableBookId(Long availableBookId) {
        this.availableBookId = availableBookId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<WaitingList> getBooksOnTheWaitingList() {
        return booksOnTheWaitingList;
    }

    public void setBooksOnTheWaitingList(List<WaitingList> booksOnTheWaitingList) {
        this.booksOnTheWaitingList = booksOnTheWaitingList;
    }

    @Override
    public String toString() {
        return "AvailableBook{" +
                "availableBookId=" + availableBookId +
                ", book=" + book +
                ", owner=" + owner +
                ", availableRentedBookList=" + availableRentedBookList +
                '}';
    }
}
