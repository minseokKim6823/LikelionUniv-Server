package likelion.univ.domain.post.dto.response;

import likelion.univ.domain.comment.dto.response.ChildCommentData;
import likelion.univ.domain.comment.dto.response.ParentCommentData;
import likelion.univ.domain.post.dto.enums.MainCategory;
import likelion.univ.domain.post.dto.enums.SubCategory;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Builder
public record PostDetailWithCommentsData(
        Long postId,
        MainCategory mainCategory,
        SubCategory subCategory,
        Long authorId,
        String authorName,
        String authorProfileImageUrl,
        Long authorOrdinal,
        String universityName,
        Boolean isFollowedAuthor, // 내가 팔로우한 글쓴이인지 (followlist.contains(post.authorId))
        Boolean isLikedPost, // 내가 좋아요 한 게시글인지
        Integer likeCount,
        String title,
        String body,
        LocalDateTime createdDate,
        List<ParentCommentData> parentComments,
        List<ChildCommentData> childComments
) {
    public String getFormattedDate() {
        String createdDate = this.createdDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        int dateLength = createdDate.length();
        return createdDate.substring(0, dateLength - 1);
    }
}
