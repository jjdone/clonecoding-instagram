package clone.chostagram.web.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PostUploadDto {

    private String text;
    private String tag;
}
