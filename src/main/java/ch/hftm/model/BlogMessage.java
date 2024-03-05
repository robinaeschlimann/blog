package ch.hftm.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogMessage implements IBlog
{
    private long id;
    private String title;
    private String description;
    private boolean valid;
}
