package pbl.GNUB.entity;


import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TAG_MAPPING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "korean_name", unique = true, nullable = false)
    private String koreanName; // 한국어 태그 이름

    @Column(name = "english_name", unique = true, nullable = false)
    private String englishName; // 영어 태그 이름
}
