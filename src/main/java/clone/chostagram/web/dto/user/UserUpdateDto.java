package clone.chostagram.web.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserUpdateDto {

    private String password;
    private String phone;
    private String name;
    private String title;
    private String website;
}
