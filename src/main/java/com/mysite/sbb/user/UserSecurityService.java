package com.mysite.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;
    //UserDetailService는 loadUserByUsername 메서드를 구현하도록 강제하는 인터페이스
    @Override
    // loadUserByUsername 메서드는 username으로 스프링 시큐리티의 사용자 객체를 조회하여 리턴하는 메서드이다.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> _siteUser = this.userRepository.findByusername(username);
        if (_siteUser.isEmpty()) {          // unsername에 해당하는 데이터가 없을 경우
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다. ");
        }
        SiteUser siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if("admin".equals(username)) {  // 사용자명이 "admin"인 경우 admin 권한 부여
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else{     // user 권한 부여
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);   // User 객체를 생성해 반환, 이 객체는 스프링 시큐리티에 사용함 (권한 리스트도 함께 전달)
    }


}
