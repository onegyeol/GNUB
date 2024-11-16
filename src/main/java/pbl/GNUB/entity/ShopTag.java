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

    @Column(name = "name", unique = true)
    private String name;

    private int hygiene;
    private int revisit;
    private int recent;
    private int delicious;
    private int goodValue;
    private int mood;
    private int fresh;
    private int kindness;
    private int alone;
    private int chilamDong;
    private int gajwaDong;

    @Column(columnDefinition = "TEXT")
    private String tags;

    private Integer value; // `value` 필드 추가

    // Getters and Setters
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
