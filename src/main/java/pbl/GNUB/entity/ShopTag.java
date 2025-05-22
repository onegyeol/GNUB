package pbl.GNUB.entity;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Setter;

import jakarta.persistence.*;
import lombok.*;

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

    private String restId;

    @Column(name = "name", unique = true)
    private String name;

    private int alone;
    private int date;
    private int delicious;
    private int fresh;
    private int goodValue;
    private int hygiene;
    private int kindness;
    private int many;
    private int mood;
    private int parking;
    private int recent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @ToString.Exclude
    private Shop shop;
}