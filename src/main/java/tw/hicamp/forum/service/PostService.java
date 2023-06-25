package tw.hicamp.forum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.hicamp.forum.model.Post;
import tw.hicamp.forum.model.PostRepository;

@Service
public class PostService {
	
	@Autowired
	private PostRepository pRepository;
	
	public Post insertPost(Post post) {
		return pRepository.save(post);
	}
	
	public Post getPostbyNo(Integer postNo) {
		Optional<Post> optional = pRepository.findById(postNo);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
	
	public List<Post> getPostbyTitle(String postTitle) {
		return pRepository.findByPostTitle(postTitle);
	}
	
	public List<Post> getAllPosts() {
		return pRepository.findAllByOrderByPostNoDesc();
	}
	
	@Transactional
	public Post updatePost(Post post) {
		Optional<Post> optional = pRepository.findById(post.getPostNo());
		
		if(optional.isPresent()) {
			Post existingPost = optional.get();
			existingPost.setPostTitle(post.getPostTitle());
			existingPost.setPostContent(post.getPostContent());
			existingPost.setPostType(post.getPostType());
			existingPost.setPostDate(post.getPostDate());
			
			if (post.getPostAlbumBase64() != null && !post.getPostAlbumBase64().trim().isEmpty()) {
	            existingPost.setPostAlbum(post.getPostAlbumBase64());
	        }
			
			return pRepository.save(existingPost);
		}
		
			return null;			
	}
	
	public boolean deletePost(Integer postNo) {
		if(pRepository.existsById(postNo)) {
			pRepository.deleteById(postNo);
			return true;
		}
		
		return false;
	}
}

