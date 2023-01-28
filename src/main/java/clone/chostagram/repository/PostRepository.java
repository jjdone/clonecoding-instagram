package clone.chostagram.repository;

import clone.chostagram.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select * " +
            "from post  " +
            "where user_id in (" +
                "select to_user_id from follow where from_user_id = :sessionId)" +
            "order by id desc", nativeQuery = true)
    Page<Post> mainStory(long sessionId, Pageable pageable);

    @Query(value = "select * " +
            "from post " +
            "where tag like :tag or tag like concat('%, ', :tag, ' %') " +
            "or tag like concat('%, ', :tag) " +
            "or tag like concat(:tag, ' ,%') " +
            "order by id desc", nativeQuery = true)
    Page<Post> searchResult(String tag, Pageable pageable);
}
