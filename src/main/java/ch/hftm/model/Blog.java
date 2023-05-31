package ch.hftm.model;

import jakarta.persistence.*;
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
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "USERID", nullable = false)
    private User user;
}
