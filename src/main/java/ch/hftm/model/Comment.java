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
public class Comment
{
    @Id
    @GeneratedValue
    @Column(name = "COMMENTID")
    private long id;
    private String text;
}
