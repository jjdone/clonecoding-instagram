package clone.chostagram.repository;

import clone.chostagram.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class UserRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired EntityManager em;

    @Test
    public void save() throws Exception {
        //given
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .name("userA")
                .password(encoder.encode("1234"))
                .email("abc@email.com")
                .phone("010-1234-5678")
                .title(null)
                .website(null)
                .profileImgUrl(null)
                .build();
        //when
        userRepository.save(user);
        em.flush();
        em.clear();
        //then
        assertThat(user.getName()).isEqualTo(userRepository.findById(user.getId()).get().getName());
        assertThat(user.getEmail()).isEqualTo(userRepository.findById(user.getId()).get().getEmail());
    }

}