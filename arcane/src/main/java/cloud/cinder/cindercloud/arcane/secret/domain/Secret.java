package cloud.cinder.cindercloud.arcane.secret.domain;

import cloud.cinder.cindercloud.arcane.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "secrets")
@Entity
@Data
@NoArgsConstructor
public class Secret {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @Builder
    public Secret(final String id, final User user) {
        this.id = id;
        this.user = user;
    }
}
