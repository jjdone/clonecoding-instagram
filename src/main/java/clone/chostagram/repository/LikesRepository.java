package clone.chostagram.repository;

import clone.chostagram.domain.Likes;
import clone.chostagram.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    void deleteLikesByPost(Post post);

    @Modifying
    @Query(value = "insert into likes(post_id, user_id) values(:postId, :userId)", nativeQuery = true)
    void likes(Long postId, Long userId);

    @Modifying
    @Query(value = "delete from likes where post_id = :postId and user_id = :user_id", nativeQuery = true)
    void unLikes(Long postId, Long userId);
}
