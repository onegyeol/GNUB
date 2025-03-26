package pbl.GNUB.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pbl.GNUB.entity.BookmarkFolder;
import pbl.GNUB.entity.Member;

public interface BookmarkFolderRepository extends JpaRepository<BookmarkFolder, Long> {
    List<BookmarkFolder> findByMember(Member member);
    Optional<BookmarkFolder> findByIdAndMember(Long id, Member member);
    Optional<BookmarkFolder> findByMember_IdAndName(Long memberId, String name);
    void deleteByMember_IdAndName(Long memberId, String name);

}
