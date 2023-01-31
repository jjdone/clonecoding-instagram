package clone.chostagram.web.controller;

import clone.chostagram.config.auth.PrincipalDetails;
import clone.chostagram.handler.exception.CustomValidationException;
import clone.chostagram.service.PostService;
import clone.chostagram.web.dto.post.PostDto;
import clone.chostagram.web.dto.post.PostUpdateDto;
import clone.chostagram.web.dto.post.PostUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/upload")
    public String upload() {
        return "post/upload";
    }

    @PostMapping
    public String uploadPost(PostUploadDto postUploadDto,
                             @RequestParam("uploadImgUrl") MultipartFile multipartFile,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (multipartFile.isEmpty()) {
            throw new CustomValidationException("이미지가 첨부되지 않았습니다.");
        }

        postService.upload(postUploadDto, multipartFile, principalDetails);
        redirectAttributes.addAttribute("id", principalDetails.getUser().getId());
        return "redirect:/user/profile";
    }

    @GetMapping("/update/{postId}")
    public String update(@PathVariable Long postId, Model model) {
        PostDto postDto = postService.getPostDto(postId);
        model.addAttribute("postDto", postDto);
        return "post/update";
    }

    @PostMapping("/update")
    public String postUpdate(PostUpdateDto postUpdateDto,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             RedirectAttributes redirectAttributes) {
        postService.update(postUpdateDto);
        redirectAttributes.addAttribute("id", principalDetails.getUser().getId());
        return "redirect:/user/profile";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long postId,
                         @AuthenticationPrincipal PrincipalDetails principalDetails,
                         RedirectAttributes redirectAttributes) {
        postService.delete(postId);
        redirectAttributes.addAttribute("id", principalDetails.getUser().getId());
        return "redirect:/user/profile";
    }

    @GetMapping("/search")
    public String search(@RequestParam String tag, Model model) {
        model.addAttribute("tag", tag);
        return "post/search";
    }

    @PostMapping("/searchForm")
    public String searchForm(String tag, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("tag", tag);
        return "redirect:/post/search";
    }

    @GetMapping("/likes")
    public String likes() {
        return "post/likes";
    }

    @GetMapping("/popular")
    public String popular() {
        return "post/popular";
    }
}
