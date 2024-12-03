package com.sunyesle.board_project.docs.support;

import com.sunyesle.board_project.common.security.CustomUserDetails;
import com.sunyesle.board_project.member.Member;
import com.sunyesle.board_project.member.dto.MemberRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        long id = annotation.id();
        MemberRole role = annotation.role();

        Member member = new Member(id, "test@email.com", "password", "name", "010-0000-0000", role);

        UserDetails userDetails = new CustomUserDetails(member);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        return context;
    }
}
