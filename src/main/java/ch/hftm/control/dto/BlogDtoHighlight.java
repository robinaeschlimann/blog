package ch.hftm.control.dto;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.HighlightProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

import java.util.List;

public record BlogDtoHighlight (
        long id,
        String title,
        String description
) {
        @ProjectionConstructor
        public BlogDtoHighlight(@IdProjection long id,
                                @HighlightProjection List<String> title,
                                @HighlightProjection List<String> description) {
                this(id, title.get(0), description.get(0));
        }

}
