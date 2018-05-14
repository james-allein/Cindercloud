package cloud.cinder.cindercloud.arcane.user.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id")
    private String externalId;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
}
