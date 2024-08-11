package fon.njt.mockfon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotBlank(message = "Username cannot be null or empty")
    private String username;
    private String password;
    @Email
    @NotBlank(message = "Email cannot be null or empty")
    private String email;
    private String name;
    private String surname;
    private String address;
    private String umcn;
    private boolean enabled; //da li je verifikovan mejl ili ne

}
