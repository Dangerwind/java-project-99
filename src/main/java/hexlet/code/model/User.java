package hexlet.code.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Setter
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@ToString(includeFieldNames = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    // id – уникальный идентификатор пользователя, генерируется автоматически
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    //    firstName - имя пользователя
    // @NotBlank
    @ToString.Include
    private String firstName;

    //    lastName - фамилия пользователя
    // @NotBlank
    @ToString.Include
    private String lastName;

    //    password - пароль
    private String password;

    //    createdAt - дата создания (регистрации) пользователя
    @CreatedDate
    private LocalDate createdAt;

    //    updatedAt – дата обновления данных пользователя
    @LastModifiedDate
    private LocalDate updatedAt;





}
