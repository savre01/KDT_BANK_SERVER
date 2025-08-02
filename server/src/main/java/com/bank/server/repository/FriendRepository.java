package com.bank.server.repository;

import com.bank.server.model.Friend;
import com.bank.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByUserAndFriend(User user, User friend);
    boolean existsByUserAndFriend(User user, User friend);
    List<Friend> findByUser(User user);
}