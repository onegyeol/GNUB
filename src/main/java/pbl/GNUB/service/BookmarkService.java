package pbl.GNUB.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pbl.GNUB.entity.Bookmark;
import pbl.GNUB.entity.BookmarkFolder;
import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.repository.BookmarkFolderRepository;
import pbl.GNUB.repository.BookmarkRepository;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.repository.ShopRepository;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkFolderRepository bookmarkFolderRepository;
    private final MemberRepository memberRepository;
    private final ShopRepository shopRepository;

    public void bookmarkShop(Long memberId, Long shopId, Long folderId){
        Member member = memberRepository.findById(memberId).orElseThrow();
        Shop shop = shopRepository.findById(shopId).orElseThrow();

        if(bookmarkRepository.existsByMemberAndShop(member, shop)) return;

        Bookmark bookmark = new Bookmark();
        bookmark.setMember(member);
        bookmark.setShop(shop);

        if(folderId != null){
            BookmarkFolder folder = bookmarkFolderRepository.findByIdAndMember(folderId, member)
                                        .orElseThrow(() -> new IllegalArgumentException("폴더 없음"));

            bookmark.setFolder(folder);

            System.out.println("🔴 folder id" + bookmark.getFolder());
        }

        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void unbookmarkShop(Long memberId, Long shopId){
        Member member = memberRepository.findById(memberId).orElseThrow();
        Shop shop = shopRepository.findById(shopId).orElseThrow();

        bookmarkRepository.deleteByMemberAndShop(member, shop);
    }

    // 내 찜 목록 전체 조회
    public List<Bookmark> getMyBookmarks(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        return bookmarkRepository.findByMember(member);
    }

    // 특정 폴더의 찜 목록 조회
    public List<Bookmark> getBookmarksByFolder(Long memberId, Long folderId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        BookmarkFolder folder = bookmarkFolderRepository.findByIdAndMember(folderId, member)
            .orElseThrow(() -> new IllegalArgumentException("폴더 없음"));

        return bookmarkRepository.findByMemberAndFolder(member, folder);
    }


     // 폴더 생성
    public BookmarkFolder createFolder(Long memberId, String folderName) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        BookmarkFolder folder = new BookmarkFolder();
        folder.setMember(member);
        folder.setName(folderName);
    
        return bookmarkFolderRepository.save(folder); // 반환!
    }

    // 폴더 전체 조회
    public List<BookmarkFolder> getMyFolders(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        return bookmarkFolderRepository.findByMember(member);
    }

    // 폴더 삭제
    public void deleteFolder(Long folderId, Long memberId) {
        BookmarkFolder folder = bookmarkFolderRepository.findByIdAndMember(folderId,
                memberRepository.findById(memberId).orElseThrow())
                .orElseThrow(() -> new IllegalArgumentException("폴더 없음"));

        bookmarkFolderRepository.delete(folder);
    }
    
    public Map<String, List<Bookmark>> getGroupedBookmarks(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();

        // 전체 폴더 목록
        List<BookmarkFolder> folders = bookmarkFolderRepository.findByMember(member);

        // 폴더별 찜 목록
        Map<String, List<Bookmark>> grouped = new LinkedHashMap<>();

        for (BookmarkFolder folder : folders) {
            List<Bookmark> bookmarks = bookmarkRepository.findByMemberAndFolder(member, folder);
            grouped.put(folder.getName(), bookmarks);
        }

        // 폴더에 속하지 않은 일반 저장
        List<Bookmark> generalBookmarks = bookmarkRepository.findByMemberAndFolderIsNull(member);
        grouped.put("모든 게시물", generalBookmarks);

        return grouped;
    }

    // 로그인한 회원이 찜한 가게인지 아닌지
    public boolean isBookmarked(Member member, Shop shop) {
        return bookmarkRepository.existsByMemberAndShop(member, shop);
    }

    @Transactional
    public void deleteFolder(Long memberId, String folderName) {
        // 북마크 먼저 삭제
        List<Bookmark> bookmarks = bookmarkRepository.findAllByMemberIdAndFolderName(memberId, folderName);
        bookmarkRepository.deleteAll(bookmarks);
        
        // 폴더 자체 삭제
        bookmarkFolderRepository.deleteByMember_IdAndName(memberId, folderName);

        // 디버깅 로그
        System.out.println("🔴 북마크와 폴더 삭제 완료");
    }
    

}
