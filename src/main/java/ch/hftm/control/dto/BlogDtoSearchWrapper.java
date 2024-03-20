package ch.hftm.control.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogDtoSearchWrapper
{
    private String searchText;
    private long resultCount;
    private List<BlogDto> blogs;
}
