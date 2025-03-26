package pbl.GNUB.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pbl.GNUB.entity.Bookmark;
import pbl.GNUB.entity.BookmarkFolder;
import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;

public interface BookmarkRepository  extends JpaRepository<Bookmark, Long>{
    List<Bookmark> findByMember(Member member);
    List<Bookmark> findByMemberAndFolder(Member member, BookmarkFolder folder);
    boolean existsByMemberAndShop(Member member, Shop shop);
    void deleteByMemberAndShop(Member member, Shop shop);
    List<Bookmark> findByMemberAndFolderIsNull(Member member);

    // 특정 회원의 폴더에 들어있는 모든 북마크 조회
    List<Bookmark> findAllByMemberIdAndFolderName(Long memberId, String folderName);

    // 폴더명으로 전체 삭제하고 싶을 경우
    void deleteAllByMemberIdAndFolderName(Long memberId, String folderName);


    

}
