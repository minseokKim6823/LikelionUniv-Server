package likelion.univ.post.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion.univ.common.response.PageResponse;
import likelion.univ.domain.post.dto.enums.PostOrderCondition;
import likelion.univ.domain.post.dto.response.PostIdData;
import likelion.univ.domain.post.dto.enums.MainCategory;
import likelion.univ.domain.post.dto.enums.SubCategory;
import likelion.univ.post.dto.request.PostCreateRequestDto;
import likelion.univ.post.dto.request.PostUpdateRequestDto;
import likelion.univ.post.dto.response.PostDetailResponseDto;
import likelion.univ.post.dto.response.PostResponseDto;
import likelion.univ.post.usecase.*;
import likelion.univ.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Tag(name = "게시글", description = "커뮤니티 APIs")
public class PostController {

    private final CreatePostUseCase createPostUseCase;
    private final EditPostUseCase editPostUsecase;
    private final DeletePostUseCase deletePostUseCase;
    private final GetPostsByCategoriesUseCase getPostsByCategoriesUseCase;
    private final GetPostDetailUseCase getPostDetailUseCase;
    private final GetPostsBySearchTitleUseCase getPostsBySearchTitleUseCase;

    /* ----- read ----- */
    @Operation(
            summary = "게시글 단일 조회",
            description =
                    """
                            ### 게시글 상세 조회 api입니다.
                            - 테스트 완료(황제철)
                            - 게시글 / 댓글에 profile image url이 없으면 boolean 타입만 전달 (url : null 포함x)""")
    @GetMapping("/community/posts/{postId}")
    public SuccessResponse<PostDetailResponseDto> findPostDetail(@PathVariable Long postId) {
        PostDetailResponseDto response = getPostDetailUseCase.execute(postId);
        return SuccessResponse.of(response);
    }

    @Operation(
            summary = "카테고리별 posts 조회",
            description =
                    """
                            ### 정렬 조건 params (oc)
                            - **CREATED_DATE_ORDER** : 최신 순 (생성 일자 기준 내림차순)
                            - **LIKE_COUNT_ORDER** : 좋아요 순 (좋아요 수 기준 내림차순)
                            - **COMMENT_COUNT_ORDER** : 댓글 순 (댓글 수 기준 내림차순)

                            ### 카테고리 params (mc, sc)
                            - **mc (main category)** : 메인 카테고리 문자열(HQ_BOARD, FREE_BOARD, OVERFLOW)
                            - **sc (sub category)** : 서브 카테고리 문자열(post 생성 참고)

                            ### 페이지네이션 params (p, s)
                            - **p (page ; 페이지 넘버)** : 1 이상의 정수
                            - **s (size ; 페이지 크기)** :  양수
                            """
    )
    @GetMapping("/community/posts")
    public SuccessResponse<PageResponse<PostResponseDto>> findCategorizedPosts(
            @RequestParam PostOrderCondition oc,
            @RequestParam MainCategory mc,
            @RequestParam SubCategory sc,
            @ParameterObject @PageableDefault(size = 5, page = 1) Pageable pageable) {

        PageResponse<PostResponseDto> response = getPostsByCategoriesUseCase.execute(oc, mc, sc, pageable);
        return SuccessResponse.of(response);
    }

    @Operation(
            summary = "게시글 제목으로 검색",
            description = """
                            ### Search Title(st)
                            - 대소문자 구분 없이 제목 일부를 검색어로 활용 가능
                            - 최신순 정렬 기준 조회
                            
                            ### Main Category(mc)
                            - **ALL**(전체 게시글 대상 검색)
                            - **HQ_BOARD**(멋대 중앙)
                            - **FREE_BOARD**(자유게시판)
                            - **OVERFLOW**(멋사 오버플로우)

                            ### Sub Category(sc)
                            - **ALL** : (mainCategory에서 "ALL"로 설정하면 sub category는 아무거나 해도 되지만, 가급적 ALL 권장)
                            - **HQ_BOARD** : NOTICE(공지사항), QNA(질문건의), HQ_INFO(정보공유)
                            - **FREE_BOARD** : FREE_INFO(정보공유), GET_MEMBER(팀원구함), GET_PROJECT(프로젝트 구함), SHOWOFF(프로젝트 자랑)
                            - **OVERFLOW** : FRONTEND(프론트엔드), BACKEND(백엔드), PM(기획), UXUI(디자인), ETC(기타)"""
    )
    @GetMapping("/community/posts/search")
    public SuccessResponse<PageResponse<PostResponseDto>> searchPost(
            @RequestParam String st,
            @RequestParam String mc,
            @RequestParam String sc,
            @ParameterObject @PageableDefault(size = 5, page = 1) Pageable pageable) {
        PageResponse<PostResponseDto> response = getPostsBySearchTitleUseCase.execute(st, mc, sc, pageable);
        return SuccessResponse.of(response);
    }

    /* ----- command ----- */
    @Operation(
            summary = "게시글을 생성",
            description =
                    """
                            ### Main Category
                            - **HQ_BOARD**(멋대 중앙)
                            - **FREE_BOARD**(자유게시판)
                            - **OVERFLOW**(멋사 오버플로우)

                            ### Sub Category
                            - **HQ_BOARD** : NOTICE(공지사항), QNA(질문건의), HQ_INFO(정보공유)
                            - **FREE_BOARD** : FREE_INFO(정보공유), GET_MEMBER(팀원구함), GET_PROJECT(프로젝트 구함), SHOWOFF(프로젝트 자랑)
                            - **OVERFLOW** : FRONTEND(프론트엔드), BACKEND(백엔드), PM(기획), UXUI(디자인), ETC(기타)""")
    @PostMapping("/community/posts/new")
    public SuccessResponse<PostIdData> createPost(@RequestBody @Valid PostCreateRequestDto request/*, BindingResult bindingResult*/) {
        PostIdData response = createPostUseCase.execute(request);
        return SuccessResponse.of(response);
    }

    @Operation(
            summary = "게시글 수정",
            description = "제목, 내용, 썸네일 수정 : 수정을 안하는 값은 기존 데이터로 넘겨줘야 함")
    @PatchMapping("/community/posts/{postId}")
    public SuccessResponse<PostIdData> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequestDto request) {
        PostIdData response = editPostUsecase.execute(postId, request);
        return SuccessResponse.of(response);
    }

    @Operation(
            summary = "게시글 hard delete",
            description = "게시글을 database로부터 hard delete")
    @DeleteMapping("/community/posts/{postId}")
    public SuccessResponse<? extends PostIdData> deletePost(@PathVariable Long postId) {
        deletePostUseCase.execute(postId);
        return SuccessResponse.empty();
    }

}
