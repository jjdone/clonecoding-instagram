package clone.chostagram.repository;

import clone.chostagram.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select p " +
            "from Post p " +
            "where p.user.id in (select f.toUser.id from Follow f where f.fromUser.id = :sessionId) " +
            "order by p.id desc")
    Page<Post> mainStory(long sessionId, Pageable pageable);

    @Query(value = "select p " +
            "from Post p " +
            "where p.tag like :tag or p.tag like concat('%, ', :tag, ' %') " +
            "or p.tag like concat('%, ', :tag) " +
            "or p.tag like concat(:tag, ' ,%') " +
            "order by p.id desc")
    Page<Post> searchResult(String tag, Pageable pageable);
}
