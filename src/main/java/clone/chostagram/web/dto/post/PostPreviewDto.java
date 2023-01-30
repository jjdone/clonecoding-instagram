package clone.chostagram.web.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPreviewDto {

    private Long id;
    private String postImgUrl;
    private Long likesCount;

    public PostPreviewDto(Long id, String postImgUrl, Long likesCount) {
        this.id = id;
        this.postImgUrl = postImgUrl;
        this.likesCount = likesCount;
    }
}
