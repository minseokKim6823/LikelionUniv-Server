package likelion.univ.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class PostCreateRequestDto {
    @NotBlank
    @Schema(description = "제목", example = "LIKELIONUNIV 프로젝트 회고", required = true)
    private String title;
    @NotBlank
    @Schema(description = "내용", example = "프로젝트 재밌었다.", required = true)
    private String body;
    @Schema(description = "썸네일 이미지", example = "string")
    private String thumbnail;
    @NotNull
    @Schema(description = "메인 카테고리", example = "FREE_BOARD", required = true)
    private String mainCategory;
    @NotNull
    @Schema(description = "서브 카테고리", example = "FREE_INFO", required = true)
    private String subCategory;
}
