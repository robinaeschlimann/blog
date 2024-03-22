package ch.hftm.lifecycle;

import ch.hftm.service.BlogService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.hibernate.search.mapper.orm.session.SearchSession;

public class Startup
{
    @Inject
    BlogService blogService;

    @Inject
    SearchSession searchSession;

    public void onStart(@Observes StartupEvent ev) throws InterruptedException {
        var blogs = blogService.getBlogs();

        if( blogs == null || blogs.isEmpty() )
        {
            return;
        }

        searchSession.massIndexer().startAndWait();

        // Do something during the application startup
    }
}
