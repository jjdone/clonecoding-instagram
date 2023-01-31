package clone.chostagram.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FollowDto {

    private Long id;
    private String name;
    private String profileImgUrl;
    private int followState;
    private int loginUser;

    public FollowDto(Long id, String name, String profileImgUrl, int followState, int loginUser) {
        this.id = id;
        this.name = name;
        this.profileImgUrl = profileImgUrl;
        this.followState = followState;
        this.loginUser = loginUser;
    }
}
