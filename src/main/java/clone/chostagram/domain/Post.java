package clone.chostagram.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String tag;
    private String text;

    @Transient
    private Long likesCount;

    @Transient
    private boolean likesState;

    private LocalDateTime createDate;

    private String postImgUrl;

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }

    @Builder
    public Post(User user, String tag, String text, Long likesCount, String postImgUrl) {
        this.user = user;
        this.tag = tag;
        this.text = text;
        this.likesCount = likesCount;
        this.postImgUrl = postImgUrl;
    }
}
