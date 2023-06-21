package univ.tuit.telegramclone.persistant.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "contacts")
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "contact_user_id")
    private Long contactUserId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_user_id", nullable = false, insertable = false, updatable = false, referencedColumnName = "user_id")
    private User user;
}
