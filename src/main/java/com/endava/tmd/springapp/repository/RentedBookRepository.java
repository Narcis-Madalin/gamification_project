package com.endava.tmd.springapp.repository;

import com.endava.tmd.springapp.entity.AvailableBook;
import com.endava.tmd.springapp.entity.RentedBook;
import com.endava.tmd.springapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentedBookRepository extends JpaRepository<RentedBook, Long> {

    List<RentedBook> getRentedBooksByBook(AvailableBook book);

    RentedBook getRentedBookByBook(AvailableBook book);

    RentedBook getRentedBookByBookAndUser(AvailableBook book, User user);

    List<RentedBook> getRentedBooksByUser(User user);
}
