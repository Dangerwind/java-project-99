package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

    @Entity
    @Getter
    @Setter
    @Table(name = "task")
    @EntityListeners(AuditingEntityListener.class)
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @ToString(includeFieldNames = true, onlyExplicitlyIncluded = false)
    public class Task implements BaseEntity {

        @Id
        @GeneratedValue(strategy = IDENTITY)
        @EqualsAndHashCode.Include
        private Long id;

        @NotNull
        @NotBlank
        private String name;

        private Long index;

        private String description;

        @ManyToOne
        //@JoinColumn(name = "task_status_id")
        private TaskStatus taskStatus;

        @ManyToOne
        //@JoinColumn(name = "assignee_id")
        private User assignee;

        @CreatedDate
        private LocalDate createdAt;


}
