package clone.chostagram.web.controller.api;

import clone.chostagram.config.auth.PrincipalDetails;
import clone.chostagram.service.CommentService;
import clone.chostagram.web.dto.CommentUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentUploadDto commentUploadDto,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return new ResponseEntity<>(
                commentService.addComment(
                        commentUploadDto.getText(),
                        commentUploadDto.getPostId(),
                        principalDetails.getUser().getId()),
                OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>("댓글 삭제 성공", OK);
    }
}
