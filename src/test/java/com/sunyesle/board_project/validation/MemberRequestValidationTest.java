package com.sunyesle.board_project.validation;

import com.sunyesle.board_project.member.MemberRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRequestValidationTest {

    @Autowired
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("MemberRequest 유효성 검사 성공")
    @Test
    void MemberRequestValid() {
        MemberRequest dto = getValidDto();

        Set<ConstraintViolation<MemberRequest>> constraintViolations = validator.validate(dto);

        assertThat(constraintViolations).isEmpty();
    }

    static Stream<Arguments> provideFieldAndInvalidValue() {
        return Stream.of(
                Arguments.of("email", null),
                Arguments.of("email", "asdfqwer"),

                Arguments.of("name", null),
                Arguments.of("name", "1234"),
                Arguments.of("name", "!!"),

                Arguments.of("phoneNumber", null),
                Arguments.of("phoneNumber", "01012345678"),
                Arguments.of("phoneNumber", "02-111-1111"),

                Arguments.of("password", null),
                Arguments.of("password", " "),
                Arguments.of("password", "!12345!"),
                Arguments.of("password", "12345678"),
                Arguments.of("password", "password12345"),
                Arguments.of("password", "password!")
        );
    }

    @DisplayName("MemberRequest 유효성 검사 실패")
    @ParameterizedTest
    @MethodSource("provideFieldAndInvalidValue")
    void MemberRequestInvalid(String fieldName, Object invalidValue) throws NoSuchFieldException, IllegalAccessException {
        MemberRequest dto = getValidDto();
        Field field = MemberRequest.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(dto, invalidValue);

        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(dto);

        assertThat(violations.size()).isGreaterThan(0);
        System.out.println(dto);
        for (ConstraintViolation<MemberRequest> violation : violations) {
            System.out.println(violation.getMessage());
        }
    }

    private MemberRequest getValidDto() {
        return new MemberRequest(
                "test@gmail.com",
                "username",
                "010-1111-1111",
                "a12345!@"
        );
    }
}
