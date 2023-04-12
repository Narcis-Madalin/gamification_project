package com.endava.tmd.springapp.repository;

import com.endava.tmd.springapp.entity.AvailableBook;
import com.endava.tmd.springapp.entity.User;
import com.endava.tmd.springapp.entity.WaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, Long> {

    List<WaitingList> getWaitingListByAvailableBook(AvailableBook availableBook);

    List<WaitingList> getWaitingListByWaitingUser(User user);

    List<WaitingList> getWaitingListByWaitingNumber(Long nr);
}
