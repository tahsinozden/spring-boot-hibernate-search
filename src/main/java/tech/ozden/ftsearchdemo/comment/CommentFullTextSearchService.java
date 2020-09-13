package tech.ozden.ftsearchdemo.comment;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.EntityContext;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
public class CommentFullTextSearchService {

    private final EntityManager entityManager;
    private final FullTextEntityManager fullTextEntityManager;

    @Autowired
    public CommentFullTextSearchService(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
        this.fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    }

    public void createOrUpdate() throws InterruptedException {
        fullTextEntityManager.createIndexer().startAndWait();
    }

    public List<Comment> search(String keyword, List<String> fields) {
        EntityContext entityContext = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Comment.class);

        // for Ngram filters, the query must not include that filter, otherwise it may fetch all the records relevant or
        // not. this is how to override the analyzer for the field
        fields.forEach(field -> entityContext.overridesForField(field, "customNgramQuery"));
        QueryBuilder queryBuilder = entityContext.get();

        Query query = queryBuilder
                .keyword()
                .onFields(fields.toArray(String[]::new))
                .matching(keyword)
                .createQuery();

        FullTextQuery jpaQuery
                = fullTextEntityManager.createFullTextQuery(query, Comment.class);

        return jpaQuery.getResultList();
    }
}
