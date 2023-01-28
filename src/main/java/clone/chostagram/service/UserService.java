package clone.chostagram.service;

import clone.chostagram.domain.User;
import clone.chostagram.handler.exception.CustomValidationException;
import clone.chostagram.repository.UserRepository;
import clone.chostagram.web.dto.UserSignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User join(UserSignupDto userDto) {

        if(userRepository.findUserByEmail(userDto.getEmail()) != null) {
            throw new CustomValidationException("이미 존재하는 메일입니다.");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .name(userDto.getName())
                .password(encoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .title(null)
                .website(null)
                .profileImgUrl(null)
                .build());
    }
}
