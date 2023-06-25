package tw.hicamp.forum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.hicamp.forum.model.Post;
import tw.hicamp.forum.model.PostComment;
import tw.hicamp.forum.model.PostCommentRepository;


@Service
public class PostCommentService {
	
	@Autowired
	private PostCommentRepository pcRepository;
	
	public PostComment insertComment(PostComment postComment) {
		return pcRepository.save(postComment);
	}
	
	@Transactional
	public PostComment updateComment(PostComment postComment) {
		Optional<PostComment> optional = pcRepository.findById(postComment.getPostCommentNo());
		
		if(optional.isPresent()) {
			PostComment existingComment = optional.get();
			existingComment.setPostComment(postComment.getPostComment());
			existingComment.setPostCommentDate(postComment.getPostCommentDate());

			return pcRepository.save(existingComment);
		}
		
		return null;
	}
	
	public boolean deleteComment(Integer postCommentNo) {
		if(pcRepository.existsById(postCommentNo)) {
			pcRepository.deleteById(postCommentNo);
			return true;
		}
		
		return false;
	}
	
	public List<PostComment> getCommentsByPostSortedByNo(Post post) {
	    return pcRepository.findByPostOrderByPostCommentNo(post);
	}

}
