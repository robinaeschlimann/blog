package ch.hftm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Indexed
public class Comment
{
    @Id
    @GeneratedValue
    @Column(name = "COMMENTID")
    private long id;
    @FullTextField(analyzer = "german")
    private String text;
}
