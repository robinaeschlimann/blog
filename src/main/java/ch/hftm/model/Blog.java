package ch.hftm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Blog {
    @Id
    @GeneratedValue
    private long id;
    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @NotNull
    @Size(min = 3, max = 1000)
    private String description;

//    @ManyToOne
//    @JoinColumn(name = "USERID", nullable = false)
//    private User user;
}
