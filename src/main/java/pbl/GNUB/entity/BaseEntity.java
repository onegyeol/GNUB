// 시간 관련 엔티티

package pbl.GNUB.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter

public class BaseEntity {
    @CreationTimestamp  
    @Column(updatable = false)
    private LocalDateTime createdTime;  // 작성 시간

    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updatedTime;  // 수정 시간
}
