package com.sunyesle.board_project.member;

import com.sunyesle.board_project.common.entity.BaseEntity;
import com.sunyesle.board_project.member.dto.MemberRole;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    protected Member() {
    }

    public Member(String email, String password, String name, String phoneNumber, MemberRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
