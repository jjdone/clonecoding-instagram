package clone.chostagram.repository;

import clone.chostagram.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findFollowByFromUserIdAndToUserId(Long from_user_id, Long to_user_id);

    @Query(value = "select count(f) from Follow f where f.toUser.id = :profiled")
    int findFollowerCountById(Long profileId);

    @Query(value = "select count(f) from Follow f where f.fromUser.id = :profileId")
    int findFollowingCountById(Long profileId);

    @Modifying
    @Query(value = "insert into follow(from_user_id), to_user_id) values(:fromUserId, :toUserId)", nativeQuery = true)
    void follow(Long fromUserId, Long toUserId);

    @Modifying
    @Query(value = "delete from Follow f where f.fromUser.id = :fromUserId and f.toUser.id = :toUserId")
    void unfollow(Long fromUserId, Long toUserId);
}
