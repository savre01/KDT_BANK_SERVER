package com.bank.server.model.chat;

import com.bank.server.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "chatmessage")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageIndex;

   @ManyToOne
    @JoinColumn(name = "chat_index")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_index")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sentTime;

}
