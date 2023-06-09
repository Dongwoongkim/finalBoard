package dongwoongkim.springbootboard;

import dongwoongkim.springbootboard.domain.category.Category;
import dongwoongkim.springbootboard.domain.comment.Comment;
import dongwoongkim.springbootboard.domain.member.Member;
import dongwoongkim.springbootboard.domain.post.Post;
import dongwoongkim.springbootboard.domain.role.Role;
import dongwoongkim.springbootboard.domain.role.RoleType;
import dongwoongkim.springbootboard.exception.role.RoleNotFoundException;
import dongwoongkim.springbootboard.repository.category.CategoryRepository;
import dongwoongkim.springbootboard.repository.member.MemberRepository;
import dongwoongkim.springbootboard.repository.member.RoleRepository;
import dongwoongkim.springbootboard.repository.comment.CommentRepository;
import dongwoongkim.springbootboard.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class initDB {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;


    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB() {
        initRoles();
        initAdmin();
        initMember();
        initCategory();
        initPost();
        initComment();
    }

    @Transactional
    public void initRoles() {
        RoleType[] values = RoleType.values();
        List<Role> roles = Arrays.stream(values).map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
    }
    @Transactional
    public void initAdmin() {
        memberRepository.save(new Member(
                "admin", passwordEncoder.encode( "1q2w3e4r@@"), "IamAdmin", "admin@naver.com",
                List.of(roleRepository.findByRoleType(RoleType.ADMIN).orElseThrow(RoleNotFoundException::new),
                        roleRepository.findByRoleType(RoleType.MANAGER).orElseThrow(RoleNotFoundException::new),
                        roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)
                )));
    }
    @Transactional
    public void initMember() {
        memberRepository.save(new Member(
                "rlaehddnd", passwordEncoder.encode( "dhjrnfma1234!!"), "dongwoongkim", "rlaehddnd0422@naver.com",
                List.of(roleRepository.findByRoleType(RoleType.USER).orElseThrow(RoleNotFoundException::new)
                )));
    }

    @Transactional
    public void initCategory() {
        Category c1 = categoryRepository.save(new Category("category1", null));
        Category c2 = categoryRepository.save(new Category("category2", c1));
        Category c3 = categoryRepository.save(new Category("category3", c1));
        Category c4 = categoryRepository.save(new Category("category4", c2));
        Category c5 = categoryRepository.save(new Category("category5", c2));
        Category c6 = categoryRepository.save(new Category("category6", c4));
        Category c7 = categoryRepository.save(new Category("category7", c3));
        Category c8 = categoryRepository.save(new Category("category8", null));
    }

    @Transactional
    public void initPost() {

        Member member = memberRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);
        for (int i = 0; i < 100; i++) {
            String title = "title" + i;
            String content = "content " + i;
            postRepository.save(new Post(title, content, 10000L, member, category, List.of()));
        }
    }


    private void initComment() {
        Member member = memberRepository.findAll().get(0);
        Post post = postRepository.findAll().get(0);
        Comment c1 = commentRepository.save(new Comment("content", member, post, null));
        Comment c2 = commentRepository.save(new Comment("content", member, post, c1));
        Comment c3 = commentRepository.save(new Comment("content", member, post, c1));
        Comment c4 = commentRepository.save(new Comment("content", member, post, c2));
        Comment c5 = commentRepository.save(new Comment("content", member, post, c2));
        Comment c6 = commentRepository.save(new Comment("content", member, post, c4));
        Comment c7 = commentRepository.save(new Comment("content", member, post, c3));
        Comment c8 = commentRepository.save(new Comment("content", member, post, null));
    }
}
