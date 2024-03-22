package ch.hftm.control.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDtoSearchWrapper
{
    private String searchText;
    private long resultCount;
    private List<CommentDto> comments;
}