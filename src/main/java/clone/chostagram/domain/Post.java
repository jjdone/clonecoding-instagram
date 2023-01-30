package clone.chostagram.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"posts"})
    private User user;

    private String tag;
    private String text;

    @Transient
    private Long likesCount;

    @Transient
    private boolean likesState;

    private LocalDateTime createDate;

    private String postImgUrl;

    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties({"post"})
    private List<Likes> likes;

    @OrderBy("id")
    @OneToMany(mappedBy = "post")
    @JsonIgnoreProperties({"post"})
    private List<Comment> comments;


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

    public void update(String tag, String text) {
        this.tag = tag;
        this.text = text;
    }

    public void updateLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public void updateLikesState(boolean likesState) {
        this.likesState = likesState;
    }

    public void makePost(Long id) {
        this.id = id;
    }
}
