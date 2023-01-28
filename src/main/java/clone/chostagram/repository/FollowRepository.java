package clone.chostagram.repository;

import clone.chostagram.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findFollowByFromUserIdAndToUserId(Long from_user_id, Long to_user_id);

    @Query(value = "select count(*) from follow where to_user_id = :profiled", nativeQuery = true)
    int findFollowerCountById(Long profileId);

    @Query(value = "select count(*) from follow where from_user_id = :profileId", nativeQuery = true)
    int findFollowingCountById(Long profileId);

    @Modifying
    @Query(value = "insert into follow(from_user_id), to_user_id) values(:fromUserId, :toUserId)", nativeQuery = true)
    void follow(Long fromUserId, Long toUserId);

    @Modifying
    @Query(value = "delete from follow where from_user_id = :fromUserId and to_user_id = :toUserId", nativeQuery = true)
    void unfollow(Long fromUserId, Long toUserId);
}
