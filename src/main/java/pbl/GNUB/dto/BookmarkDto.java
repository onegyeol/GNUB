package pbl.GNUB.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pbl.GNUB.entity.Bookmark;

@Data
@Builder
@AllArgsConstructor
public class BookmarkDto {
    private Long shopId;
    private String shopName;
    private String shopImgUrl;
    private int likeCount;

    public static BookmarkDto from(Bookmark bookmark) {
        return BookmarkDto.builder()
                .shopId(bookmark.getShop().getId())
                .shopName(bookmark.getShop().getName())
                .shopImgUrl(bookmark.getShop().getImgUrl())
                .likeCount(bookmark.getShop().getLikeCount())
                .build();
    }
}
