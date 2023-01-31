package clone.chostagram.service;

import clone.chostagram.handler.exception.CustomApiException;
import clone.chostagram.repository.FollowRepository;
import clone.chostagram.web.dto.FollowDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final EntityManager em;

    @Transactional
    public void follow(Long fromUserId, Long toUserId) {
        if (followRepository.findFollowByFromUserIdAndToUserId(fromUserId, toUserId) != null) {
            throw new CustomApiException("이미 팔로우 하였습니다.");
        }

        followRepository.follow(fromUserId, toUserId);
    }

    @Transactional
    public void unfollow(Long fromUserId, Long toUserId) {
        followRepository.unfollow(fromUserId, toUserId);
    }

    @Transactional
    public List<FollowDto> getFollower(Long profileId, Long loginId) {
        String query = "select u.id, u.name, u.profileImgUrl, " +
                "case when (select 1 from follow where fromUser.id = :fromUserId) and toUser.id = u.id then TRUE else FALSE as followState, " +
                "case u.id when :userId then TRUE else FALSE end as loginUser " +
                "from user u, follow f " +
                "where u.id = f.fromUser.id and f.toUser.id = :toUserId";

        return em.createQuery(query, FollowDto.class)
                .setParameter("fromUserId", loginId)
                .setParameter("userId", loginId)
                .setParameter("toUserId", profileId)
                .getResultList();
    }

    @Transactional
    public List<FollowDto> getFollowing(Long profileId, Long loginId) {
        String query = "select u.id, u.name, u.profileImgUrl, " +
                "case when (select 1 from follow where fromUser.id = :fromUserId1) and toUser.id = u.id then TRUE else FALSE as followState, " +
                "case u.id when :userId then TRUE else FALSE end as loginUser " +
                "from user u, follow f " +
                "where u.id = f.toUser.id and f.fromUser.id = :fromUserId2";

        return em.createQuery(query, FollowDto.class)
                .setParameter("fromUserId1", loginId)
                .setParameter("userId", loginId)
                .setParameter("fromUserId2", profileId)
                .getResultList();
    }
}
