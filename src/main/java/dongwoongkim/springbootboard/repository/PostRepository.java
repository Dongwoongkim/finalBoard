package dongwoongkim.springbootboard.repository;

import dongwoongkim.springbootboard.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
