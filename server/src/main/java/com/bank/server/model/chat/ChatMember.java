package com.bank.server.model.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.bank.server.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(ChatMemberId.class)
@Table(name = "chat_member")
public class ChatMember {

    @Id
    @ManyToOne
    @JoinColumn(name = "chatIndex")
    @JsonIgnore
    private Chat chat;

    @Id
    @ManyToOne
    @JoinColumn(name = "userIndex")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joined;
}
