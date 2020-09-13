package tech.ozden.ftsearchdemo.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@RunWith(SpringRunner.class)
class CommentFullTextSearchServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentFullTextSearchService commentFullTextSearchService;

    @BeforeEach
    public void beforeEachTest() throws InterruptedException {
        commentRepository.deleteAll();
        List<Comment> comments = List.of(
                new Comment(null, "Wrong item", "Jack", "here is a wrong item"),
                new Comment(null, "good value / price", "a smart developer", "really good content"),
                new Comment(null, "recommends the seller", "John Doe", "public comment"),
                new Comment(null, "not happy with the product", "Cesar", "definitely you need to find sth better"),
                new Comment(null, "happy with the product", "Cesar", "find something else")
        );
        commentRepository.saveAll(comments);
        commentFullTextSearchService.createOrUpdate();
    }

    @Test
    public void shouldSearchTitleInTheComment() {
        // given
        String keyword = "c com";

        // when
        List<Comment> actual = commentFullTextSearchService.search(keyword, List.of("content"));

        // then
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getContent()).isEqualTo("public comment");
    }

    @Test
    public void shouldSearchTitleInSeveralFields() {
        // given
        String keyword = "happy ";

        // when
        List<Comment> actual = commentFullTextSearchService.search(keyword, List.of("title", "author", "content"));

        // then
        assertThat(actual).hasSize(2);
    }
}
