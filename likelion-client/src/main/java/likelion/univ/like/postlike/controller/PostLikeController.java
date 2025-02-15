package likelion.univ.like.postlike.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion.univ.like.postlike.dto.PostLikeRequestDto;
import likelion.univ.like.postlike.usecase.CreateOrDeletePostLikeUseCase;
import likelion.univ.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/community/post-likes")
@Tag(name = "게시글 좋아요", description = "커뮤니티 APIs")
public class PostLikeController {
    private final CreateOrDeletePostLikeUseCase createOrDeletePostLikeUseCase;

    @Operation(summary = "게시글 좋아요 수행", description = "게시글 좋아요를 생성함")
    @PostMapping("")
    public SuccessResponse<?> createPostLike(@RequestBody PostLikeRequestDto request) {
        createOrDeletePostLikeUseCase.execute(request);
        return SuccessResponse.empty();
    }

//    @Operation(summary = "게시글 좋아요 취소", description = "게시글 좋아요를 hard delete함")
//    @DeleteMapping("")
//    public SuccessResponse<?> deletePostLike(@RequestParam Long postId) {
//        deletePostLikeUseCase.execute(postId);
//        return SuccessResponse.empty();
//    }
}
