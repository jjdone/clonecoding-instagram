package clone.chostagram.web.controller;

import clone.chostagram.config.auth.PrincipalDetails;
import clone.chostagram.service.UserService;
import clone.chostagram.web.dto.user.UserProfileDto;
import clone.chostagram.web.dto.user.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Model model, @RequestParam Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserProfileDto userProfileDto = userService.getUserProfileDto(id, principalDetails.getUser().getId());
        model.addAttribute("userProfileDto", userProfileDto);
        return "user/profile";
    }

    @GetMapping("/update")
    public String update() {
        return "user/update";
    }

    @PostMapping("/update")
    public String updateUser(@Valid UserUpdateDto userUpdateDto,
                             @RequestParam("profileImgUrl") MultipartFile multipartFile,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.update(userUpdateDto, multipartFile, principalDetails);
        redirectAttributes.addAttribute("id", principalDetails.getUser().getId());
        return "redirect:/user/profile";
    }
}