package clone.chostagram.web.controller.api;

import clone.chostagram.config.auth.PrincipalDetails;
import clone.chostagram.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowApiController {

    private final FollowService followService;

    @PostMapping("/{toUserId}")
    public ResponseEntity<?> followUser(@PathVariable Long toUserId,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        followService.follow(principalDetails.getUser().getId(), toUserId);
        return new ResponseEntity<>("팔로우 성공", OK);
    }

    @DeleteMapping("/{toUserId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long toUserId,
                                          @AuthenticationPrincipal PrincipalDetails principalDetails) {
        followService.unfollow(principalDetails.getUser().getId(), toUserId);
        return new ResponseEntity<>("팔로수 취소 성공", OK);
    }
}
