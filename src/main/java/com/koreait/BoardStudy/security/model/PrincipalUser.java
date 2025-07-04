package com.koreait.BoardStudy.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koreait.BoardStudy.entity.UserRole;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class PrincipalUser implements UserDetails {
    private Integer userId;
    private String userName;
    @JsonIgnore
    private String password;
    private String userEmail;
    private List<UserRole> userRoles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles.stream().map(userRole
                -> new SimpleGrantedAuthority(userRole.getRole().getRoleName()))
                .collect(Collectors.toList());
        //부여된 권한으로 객체를 생성하고 리스트에 담아서 반환
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
}
