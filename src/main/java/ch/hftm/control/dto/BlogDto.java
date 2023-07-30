package ch.hftm.control.dto;

import ch.hftm.model.Blog;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogDto
{
    private long id;
    @NotNull @Size(min = 3, max = 50)
    private String title;

    @NotNull @Size(min = 3, max = 1000)
    private String description;

    public static BlogDto toDto( Blog blog )
    {
        return BlogDto.builder()
                .id( blog.getId() )
                .title( blog.getTitle() )
                .description( blog.getDescription() )
                .build();
    }

    public static Blog toBlog(BlogDto blog )
    {
        return Blog.builder()
                .id( blog.getId() )
                .title( blog.getTitle() )
                .description( blog.getDescription() )
                .build();
    }
}
