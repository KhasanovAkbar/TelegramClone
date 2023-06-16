package univ.tuit.telegramclone.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.tuit.telegramclone.entity.enums.UserStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long userId;
    // private String firstName;
    //  private String lastName;
    private String userName;
    //  private String phoneNumber;
    // private String password;
    private String createdAt;
    private String updatedAt;
    //  private String profilePhoto;
    private UserStatus userStatus;
}
