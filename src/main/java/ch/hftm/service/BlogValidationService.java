package ch.hftm.service;

import ch.hftm.model.BlogMessage;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class BlogValidationService
{
    @Inject
    BlogService blogService;

    @Incoming("blog-valid-text")
    @Blocking
    public void consume(BlogMessage blog)
    {
        blogService.updateBlog(blog);
    }
}
