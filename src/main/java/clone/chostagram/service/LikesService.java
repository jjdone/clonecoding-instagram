package clone.chostagram.service;

import clone.chostagram.handler.exception.CustomApiException;
import clone.chostagram.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;

    @Transactional
    public void likes(Long postId, Long sessionId) {
        try {
            likesRepository.likes(postId, sessionId);
        } catch (Exception e) {
            throw new CustomApiException("이미 좋아요 하였습니다.");
        }
    }

    @Transactional
    public void unLikes(Long postId, Long sessionId) {
        likesRepository.unLikes(postId, sessionId);
    }
}
