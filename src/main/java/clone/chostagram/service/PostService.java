package clone.chostagram.service;

import clone.chostagram.config.auth.PrincipalDetails;
import clone.chostagram.domain.Post;
import clone.chostagram.domain.User;
import clone.chostagram.repository.CommentRepository;
import clone.chostagram.repository.LikesRepository;
import clone.chostagram.repository.PostRepository;
import clone.chostagram.repository.UserRepository;
import clone.chostagram.web.dto.post.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final EntityManager em;

    @Value("${custom.post.path}")
    private String uploadUrl;

    @Transactional
    public void upload(PostUploadDto postUploadDto, MultipartFile multipartFile, PrincipalDetails principalDetails) {
        UUID uuid = UUID.randomUUID();
        String imgFileName = uuid + "_" + multipartFile.getOriginalFilename();

        Path imageFilePath = Paths.get(uploadUrl + imgFileName);
        try {
            Files.write(imageFilePath, multipartFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        postRepository.save(Post.builder()
                .postImgUrl(imgFileName)
                .tag(postUploadDto.getTag())
                .text(postUploadDto.getText())
                .user(principalDetails.getUser())
                .likesCount(0L)
                .build());
    }

    @Transactional
    public PostInfoDto getPostInfoDto(Long postId, Long sessionId) {
        Post post = postRepository.findById(postId).get();

        PostInfoDto postInfoDto = new PostInfoDto();
        postInfoDto.setId(postId);
        postInfoDto.setTag(post.getTag());
        postInfoDto.setText(post.getText());
        postInfoDto.setPostImgUrl(post.getPostImgUrl());
        postInfoDto.setCreateDate(post.getCreateDate());

        postInfoDto.setLikesCount(Long.valueOf(post.getLikes().size()));
        post.getLikes().forEach(likes -> {
            if(likes.getUser().getId() == sessionId) postInfoDto.setLikeState(true);
        });
        postInfoDto.setComments(post.getComments());

        User user = userRepository.findById(post.getUser().getId()).get();

        postInfoDto.setPostUploader(user);
        if (sessionId == post.getUser().getId()) {
            postInfoDto.setUploader(true);
        } else {
            postInfoDto.setUploader(false);
        }

        return postInfoDto;
    }

    @Transactional
    public PostDto getPostDto(Long postId) {
        Post post = postRepository.findById(postId).get();

        return PostDto.builder()
                .id(postId)
                .tag(post.getTag())
                .text(post.getText())
                .postImgUrl(post.getPostImgUrl())
                .build();
    }

    @Transactional
    public void update(PostUpdateDto postUpdateDto) {
        Post post = postRepository.findById(postUpdateDto.getId()).get();
        post.update(postUpdateDto.getTag(), postUpdateDto.getText());
    }

    @Transactional
    public void delete(Long postId) {
        Post post = postRepository.findById(postId).get();

        likesRepository.deleteLikesByPost(post);
        commentRepository.deleteCommentByPost(post);
        File file = new File(uploadUrl + post.getPostImgUrl());
        file.delete();
        postRepository.deleteById(postId);
    }

    @Transactional
    public Page<Post> getPost(Long sessionId, Pageable pageable) {
        Page<Post> posts = postRepository.mainStory(sessionId, pageable);

        posts.forEach(post -> {
            post.updateLikesCount((long) post.getLikes().size());
            post.getLikes().forEach(like -> {
                if(like.getUser().getId() == sessionId) post.updateLikesState(true);
            });
        });

        return posts;
    }

    @Transactional
    public Page<Post> getTagPost(String tag, Long sessionId, Pageable pageable) {
        Page<Post> posts = postRepository.searchResult(tag, pageable);

        posts.forEach(post -> {
            post.updateLikesCount((long) post.getLikes().size());
            post.getLikes().forEach(like -> {
                if(like.getUser().getId() == sessionId) post.updateLikesState(true);
            });
        });

        return posts;
    }

    @Transactional
    public Page<PostPreviewDto> getLikesPost(Long sessionId, Pageable pageable) {

        String query = "select p.id from Likes l, Post p where l.user = :userId and p.id = l.post.id";

        List<Long> postIdList = em.createQuery(query, Long.class)
                .setParameter("userId", sessionId)
                .getResultList();

        String jpql = "select p.id, p.postImgUrl, count(p.id) as likesCount " +
                "from Likes l, Post p " +
                "where l.post.id = p.id " +
                "and p.id in (:postIdList) " +
                "group by p.id " +
                "order by p.id";

        List<PostPreviewDto> postLikesList = em.createQuery(jpql, PostPreviewDto.class)
                .setParameter("postIdList", postIdList)
                .getResultList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), postLikesList.size());

        if (start > postLikesList.size()) {
            return new PageImpl<>(postLikesList.subList(0, 0), pageable, 0);
        }

        return new PageImpl<>(postLikesList.subList(start, end), pageable, postLikesList.size());
    }

    @Transactional
    public List<PostPreviewDto> getPopularPost() {
        List postIdList = em.createQuery("select p.id from Likes l, Post p where p.id = l.post.id")
                .getResultList();

        String jpql = "select p.id, p.postImgUrl, count(p.id) as likesCount " +
                "from Likes l, Post p " +
                "where l.post.id = p.id " +
                "and p.id in :postIdList " +
                "group by p.id " +
                "order by likesCount desc, p.id ";

        return em.createQuery(jpql, PostPreviewDto.class)
                .setParameter("postIdList", postIdList)
                .setMaxResults(12)
                .getResultList();
    }
}
