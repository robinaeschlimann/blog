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

        context.analyzer( "titleAnalyzer" )
                .custom()
                .tokenizer( "standard" )
                .tokenFilters( "lowercase", "asciifolding" );

        /*context.tokenFilter( "autocomplete_edge_ngram" )
                .type( "edge_ngram" )
                .param( "min_gram", 1 )
                .param( "max_gram", 10 );*/

        context.normalizer( "sort" ).custom()
                .tokenFilters( "lowercase", "asciifolding" );
    }
}
