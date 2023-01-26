package clone.chostagram.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "post_user",
                        columnNames = {"post_id", "user_id"}
                )
        }
)
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Likes(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
