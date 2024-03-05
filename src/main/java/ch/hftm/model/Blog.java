package ch.hftm.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Blog extends PanacheEntityBase implements IBlog {
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @NotNull
    @Size(min = 3, max = 1000)
    private String description;

    private boolean valid;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "BLOGID", nullable = false)
    private List<Comment> comments;

//    @ManyToOne
//    @JoinColumn(name = "USERID", nullable = false)
//    private User user;
}
