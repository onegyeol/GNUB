package pbl.GNUB.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@ToString
@Table(name = "SHOPTAG")
@Builder
public class ShopTag {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "hygiene")
    private int hygiene;

    @Column(name = "revisit")
    private int revisit;

    @Column(name = "recent")
    private int recent;

    @Column(name = "delicious")
    private int delicious;

    @Column(name = "good_value")
    private int goodValue;

    @Column(name = "mood")
    private int mood;

    @Column(name = "fresh")
    private int fresh;

    @Column(name = "kindness")
    private int kindness;

    @Column(name = "alone")
    private int alone;

    @Column(name = "chilam_dong")
    private int chilamDong;

    @Column(name = "gajwa_dong")
    private int gajwaDong;
}
