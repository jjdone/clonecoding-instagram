package clone.chostagram.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    private String phone;

    private String title;
    private String website;

    @Column(name = "profile_image_url")
    private String profileImgUrl;

    @Builder
    public User(String name, String password, String email, String phone, String title, String website, String profileImgUrl) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.title = title;
        this.website = website;
        this.profileImgUrl = profileImgUrl;
    }
}
