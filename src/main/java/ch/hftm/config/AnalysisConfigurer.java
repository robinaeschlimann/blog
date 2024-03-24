package ch.hftm.config;

import io.quarkus.hibernate.search.orm.elasticsearch.SearchExtension;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

@SearchExtension
public class AnalysisConfigurer implements ElasticsearchAnalysisConfigurer
{

    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context)
    {

        context.analyzer( "title" )
                .custom()
                .tokenizer( "standard" )
                .tokenFilters( "lowercase", "asciifolding" );

        context.normalizer( "sort" ).custom()
                .tokenFilters( "lowercase", "asciifolding" );
    }
}
