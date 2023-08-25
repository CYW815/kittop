package com.kittop.dto;

import com.kittop.domain.Role;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

    private Long userId;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 14, message = "비밀번호는 8자 이상, 14자 이하로 입력해주세요.")
    private String password;

    @NotEmpty(message = "닉네임은 필수 입력 값입니다.")
    private String nickName;

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "생년월일은 필수 입력 값입니다.")
    private String birth;

    private String gender;    //'M','W'

    private String addr;

    private String phone;

    private Timestamp regDate;

    private Timestamp updateDate;

    private String uuid;
    private Role role;  //ADMIN,USER

}