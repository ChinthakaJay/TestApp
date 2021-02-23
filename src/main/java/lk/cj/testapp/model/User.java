package lk.cj.testapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE user SET deleted=1 WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = 0")
public class User {
    private final int deleted = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String adId;

    private String email;

    private String firebaseToken;
}
