package com.sunyesle.board_project.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MemberRequest {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private final String email;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "이름은 영문 대소문자 및 한글만 포함할 수 있습니다.")
    private final String name;

    @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다. (예: 010-1234-5678)")
    private final String phoneNumber;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=(?:.*\\d){5,20})(?=(?:.*[@$!%*?&]){2,20}).{5,20}$",
            message = "비밀번호는 영문을 포함하고, 숫자가 5개 이상, 특수문자가 2개 이상 포함되어야 합니다."
    )
    private final String password;

    public MemberRequest(String email, String name, String phoneNumber, String password) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
