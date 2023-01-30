package clone.chostagram.service;

import clone.chostagram.config.auth.PrincipalDetails;
import clone.chostagram.domain.User;
import clone.chostagram.handler.exception.CustomValidationException;
import clone.chostagram.repository.FollowRepository;
import clone.chostagram.repository.UserRepository;
import clone.chostagram.web.dto.user.UserProfileDto;
import clone.chostagram.web.dto.user.UserSignupDto;
import clone.chostagram.web.dto.user.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Value("${custom.profileImg.path}")
    private String uploadFolder;

    @Transactional
    public User join(UserSignupDto userSignupDto) {

        if(userRepository.findUserByEmail(userSignupDto.getEmail()) != null) {
            throw new CustomValidationException("이미 존재하는 메일입니다.");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .name(userSignupDto.getName())
                .password(encoder.encode(userSignupDto.getPassword()))
                .email(userSignupDto.getEmail())
                .phone(userSignupDto.getPhone())
                .title(null)
                .website(null)
                .profileImgUrl(null)
                .build());
    }

    @Transactional
    public void update(UserUpdateDto userUpdateDto, MultipartFile multipartFile, PrincipalDetails principalDetails) {
        User user = userRepository.findById(principalDetails.getUser().getId())
                .orElseThrow(() -> {
                    return new CustomValidationException("찾을 수 없는 user입니다.");
                });
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!multipartFile.isEmpty()) {
            String imageFileName = user.getId() + "_" + multipartFile.getOriginalFilename();
            Path imageFilePath = Paths.get(uploadFolder + imageFileName);
            try {
                if (user.getProfileImgUrl() != null) {
                    File file = new File(uploadFolder + user.getProfileImgUrl());
                    file.delete();
                }
                Files.write(imageFilePath, multipartFile.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            user.updateProfileImgUrl(imageFileName);
        }

        user.update(
                userUpdateDto.getName(),
                encoder.encode(userUpdateDto.getPassword()),
                userUpdateDto.getPhone(),
                userUpdateDto.getTitle(),
                userUpdateDto.getWebsite()
        );

        principalDetails.updateUser(user);
    }

    @Transactional
    public UserProfileDto getUserProfileDto(Long profileId, Long sessionId) {
        UserProfileDto userProfileDto = new UserProfileDto();

        User findUser = userRepository.findById(profileId).orElseThrow(() -> {
            return new CustomValidationException("찾을 수 없는 user입니다.");
        });
        userProfileDto.setUser(findUser);
        userProfileDto.setPostCount(findUser.getPosts().size());

        User loginUser = userRepository.findById(sessionId).orElseThrow(() -> {
            return new CustomValidationException("찾을 수 없는 user입니다.");
        });
        userProfileDto.setLoginUser(loginUser.getId() == loginUser.getId());

        userProfileDto.setFollow(followRepository.findFollowByFromUserIdAndToUserId(loginUser.getId(), findUser.getId()) != null);

        userProfileDto.setUserFollowerCount(followRepository.findFollowerCountById(profileId));
        userProfileDto.setUserFollowingCount(followRepository.findFollowingCountById(profileId));

        findUser.getPosts().forEach(post -> {
            post.updateLikesCount(Long.valueOf(post.getLikes().size()));
        });

        return userProfileDto;
    }
}
