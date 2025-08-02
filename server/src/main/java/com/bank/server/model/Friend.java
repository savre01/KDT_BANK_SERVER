package com.bank.server.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friend", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_index", "friend_index"})
})
@Getter @Setter @NoArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendId;

    @ManyToOne
    @JoinColumn(name = "user_index", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_index", nullable = false)
    private User friend;

    public Friend(Long friendId, User user, User friend) {
        this.friendId = friendId;
        this.user = user;
        this.friend = friend;
    }
}