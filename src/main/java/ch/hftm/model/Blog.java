package ch.hftm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Indexed
public class Blog implements IBlog {
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    @Size(min = 3, max = 50)
    @FullTextField(analyzer = "german")
    private String title;

    @NotNull
    @Size(min = 3, max = 1000)
    @FullTextField(analyzer = "german")

    private String description;

    private boolean valid;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "BLOGID", nullable = false)
    private List<Comment> comments;

//    @ManyToOne
//    @JoinColumn(name = "USERID", nullable = false)
//    private User user;
}
