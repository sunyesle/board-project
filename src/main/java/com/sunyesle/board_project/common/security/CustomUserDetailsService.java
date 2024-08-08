package com.sunyesle.board_project.common.security;

import com.sunyesle.board_project.common.exception.ErrorCodeException;
import com.sunyesle.board_project.common.exception.MemberErrorCode;
import com.sunyesle.board_project.member.Member;
import com.sunyesle.board_project.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new ErrorCodeException(MemberErrorCode.MEMBER_NOT_FOUND));

        return new CustomUserDetails(member);
    }
}
