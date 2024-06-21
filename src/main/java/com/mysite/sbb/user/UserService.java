package com.mysite.sbb.user;

import com.mysite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String password){
        SiteUser user = new SiteUser();
        user.setUsername(username);
//        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));     // password는 암화화하여 저장한다. password 객체를 sequrityconfig에서 빈으로 등록하여사용
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String username){
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()){      // siteUser가 존재할 경우
            return siteUser.get();
        }else{
            throw new DataNotFoundException("siteuser not found");
        }
    }
}
