package com.example.todo.userapi.dto;

import com.example.todo.userapi.entity.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter @Getter
@ToString @EqualsAndHashCode(of = "email")
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserRequestSignUpDTO {

    @NotBlank
    @Email // @가 들어갔는지 등 검증 해주는 어노테이션
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Size(min = 2, max = 6)
    private String username;

    // dto를 Entity로 변경하는 메서드
    public User toEntity() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .userName(this.username)
                .build();
    }

}
