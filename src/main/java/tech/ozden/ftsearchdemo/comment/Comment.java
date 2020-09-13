package tech.ozden.ftsearchdemo.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

@Indexed
@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    @Field(analyzer = @Analyzer(definition = "customNgramIndexer"))
    private String title;

    @Column
    @Field(analyzer = @Analyzer(definition = "customNgramIndexer"))
    private String author;

    @Column
    @Field(analyzer = @Analyzer(definition = "customNgramIndexer"))
    private String content;
}
