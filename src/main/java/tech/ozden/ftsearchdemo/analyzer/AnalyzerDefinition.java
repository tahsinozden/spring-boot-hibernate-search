package tech.ozden.ftsearchdemo.analyzer;

import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.hibernate.search.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Indexed
@AnalyzerDef(name = "customNgramIndexer",
        // consider searching the full text
        tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),
        filters = {
                // make all lower case
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                // generate ngram indexes
                @TokenFilterDef(factory = NGramFilterFactory.class,
                        params = {
                                @Parameter(name = "minGramSize", value = "3"),
                                @Parameter(name = "maxGramSize", value = "40")})
        })
@AnalyzerDef(name = "customNgramQuery",
        // consider searching the full text
        tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),
        filters = {
                // make all lower case
                @TokenFilterDef(factory = LowerCaseFilterFactory.class)
        })
@Entity
@Table(name = "analyzer_definition")
public class AnalyzerDefinition {
    @Id
    private Long id;
}
