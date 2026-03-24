package models;

import lombok.*;

@Data
@AllArgsConstructor
public class UserModel {
    private String email;
    private String password;
    private String name;
}