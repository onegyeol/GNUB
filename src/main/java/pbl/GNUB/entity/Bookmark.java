package pbl.GNUB.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="BOOKMARK")
@Getter @Setter
public class Bookmark {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Shop shop;

    @ManyToOne
    private BookmarkFolder folder;
    
}
