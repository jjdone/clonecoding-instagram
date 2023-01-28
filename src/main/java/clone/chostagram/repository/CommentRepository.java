package clone.chostagram.repository;

import clone.chostagram.domain.Comment;
import clone.chostagram.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteCommentByPost(Post post);
}
