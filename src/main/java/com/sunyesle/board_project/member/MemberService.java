package com.sunyesle.board_project.member;

import com.sunyesle.board_project.common.dto.CreateResponse;
import com.sunyesle.board_project.common.exception.ErrorCodeException;
import com.sunyesle.board_project.common.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CreateResponse signup(MemberRequest request) {
        if(memberRepository.findByEmail(request.getEmail()).isPresent()){
            throw new ErrorCodeException(MemberErrorCode.MEMBER_EMAIL_DUPLICATE);
        }

        Member member = new Member(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getPhoneNumber(),
                MemberRole.USER
        );
        memberRepository.save(member);
        return new CreateResponse(member.getId());
    }
}
