package clone.chostagram.service;

import clone.chostagram.domain.Comment;
import clone.chostagram.domain.Post;
import clone.chostagram.domain.User;
import clone.chostagram.handler.exception.CustomApiException;
import clone.chostagram.repository.CommentRepository;
import clone.chostagram.repository.PostRepository;
import clone.chostagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment addComment(String text, Long postId, Long sessionId) {
        Post post = postRepository.findById(postId).get();
        User user = userRepository.findById(sessionId).orElseThrow(() -> {
            return new CustomApiException("유저 아이디를 찾을 수 없습니다.");
        });

        Comment comment = Comment.builder()
                .text(text)
                .post(post)
                .user(user)
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
