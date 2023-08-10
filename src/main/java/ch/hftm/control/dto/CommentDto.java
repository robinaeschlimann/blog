package ch.hftm.control.dto;

import ch.hftm.model.Comment;
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
public class CommentDto
{
    private long id;

    @NotNull @Size(min = 3, max = 1000)
    private String text;

    public static Comment toComment( CommentDto commentDto )
    {
        return Comment.builder()
            .id(commentDto.getId() )
            .text( commentDto.getText() )
            .build();
    }

    public static CommentDto toDto( Comment comment )
    {
        return CommentDto.builder()
            .id(comment.getId() )
            .text( comment.getText() )
            .build();
    }
}
