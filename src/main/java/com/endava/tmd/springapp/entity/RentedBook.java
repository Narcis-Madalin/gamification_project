package com.endava.tmd.springapp.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rented_books")
public class RentedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rented_book_id", nullable = false, updatable = false, columnDefinition = "SERIAL")
    private Long rentedBookId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "available_book_id")
    AvailableBook book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;

    @Column(name = "rented_until", nullable = false)
    private LocalDateTime rentedUntil;


    @Column(name = "rented_period")
    private String rentedPeriod;

    @Column(name = "was_period_extended")
    private Boolean extendedPeriod;

    public RentedBook(){

    }

    public Long getRentedBookId() {
        return rentedBookId;
    }


    public void setRentedBookId(Long rentedBookId) {
        this.rentedBookId = rentedBookId;
    }

    public AvailableBook getBook() {
        return book;
    }

    public void setBook(AvailableBook book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getRentedUntil() {
        return rentedUntil;
    }

    public void setRentedUntil(LocalDateTime rentedUntil) {
        this.rentedUntil = rentedUntil;
    }

    public String getRentedPeriod() {
        return rentedPeriod;
    }

    public void setRentedPeriod(String rentedPeriod) {
        this.rentedPeriod = rentedPeriod;
    }

    public Boolean getExtendedPeriod() {
        return extendedPeriod;
    }

    public void setExtendedPeriod(Boolean extendedPeriod) {
        this.extendedPeriod = extendedPeriod;
    }

    @Override
    public String toString() {
        return "RentedBook{" +
                "rentedBookId=" + rentedBookId +
                ", book=" + book +
                ", user=" + user +
                ", rentedUntil=" + rentedUntil +
                ", rentedPeriod='" + rentedPeriod + '\'' +
                '}';
    }
}
