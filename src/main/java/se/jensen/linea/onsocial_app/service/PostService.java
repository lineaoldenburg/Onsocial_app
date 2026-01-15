package se.jensen.linea.onsocial_app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.jensen.linea.onsocial_app.DTO.PostRequestDTO;
import se.jensen.linea.onsocial_app.DTO.PostResponseDTO;
import se.jensen.linea.onsocial_app.model.Post;
import se.jensen.linea.onsocial_app.model.User;
import se.jensen.linea.onsocial_app.repository.PostRepository;
import se.jensen.linea.onsocial_app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // Find all posts
    @Transactional(readOnly = true)
    public List<PostResponseDTO> findAll() {
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        List<Post> posts = postRepository.findAll();

        for (Post post : posts) {
            PostResponseDTO dto = new PostResponseDTO(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getUser().getId(),
                    post.getUser().getAlias(),
                    post.getCreated()
            );
            postResponseDTOList.add(dto);
        }
        return postResponseDTOList;
    }

    // Find all posts by user ID
    @Transactional(readOnly = true)
    public List<PostResponseDTO> findAllByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();

        for (Post post : posts) {
            PostResponseDTO dto = new PostResponseDTO(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getUser().getId(),
                    post.getUser().getAlias(),
                    post.getCreated()
            );
            postResponseDTOList.add(dto);
        }
        return postResponseDTOList;
    }

    // Find specific post by ID - returns one post or throws exception
    @Transactional(readOnly = true)
    public PostResponseDTO findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getAlias(),
                post.getCreated()
        );
    }

    // Create new post
    @Transactional
    public PostResponseDTO createPost(Long userId, PostRequestDTO postDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Post post = new Post();
        post.setTitle(postDto.title());
        post.setContent(postDto.content());
        post.setUser(user);

        Post savedPost = postRepository.save(post);

        return new PostResponseDTO(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getUser().getId(),
                savedPost.getUser().getAlias(),
                savedPost.getCreated()
        );
    }

    // Update post
    @Transactional
    public PostResponseDTO updatePost(Long id, PostRequestDTO postRequestDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        post.setTitle(postRequestDTO.title());
        post.setContent(postRequestDTO.content());

        Post updatedPost = postRepository.save(post);

        return new PostResponseDTO(
                updatedPost.getId(),
                updatedPost.getTitle(),
                updatedPost.getContent(),
                updatedPost.getUser().getId(),
                updatedPost.getUser().getAlias(),
                updatedPost.getCreated()
        );
    }

    // Delete post
    public boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }
}