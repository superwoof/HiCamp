package tw.hicamp.forum.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.hicamp.forum.model.Post;
import tw.hicamp.forum.model.PostComment;
import tw.hicamp.forum.service.PostCommentService;
import tw.hicamp.forum.service.PostService;


@Controller
public class UpdatePostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private PostCommentService postcommentService;
	
	// 修改貼文
	@GetMapping("/forum/update/{postNo}")
	public String updatePostMain(@PathVariable("postNo") Integer postNo, Model model) {
		Post originalForum = postService.getPostbyNo(postNo);
		model.addAttribute("originalForum", originalForum);
		model.addAttribute("post", originalForum);
		return "/forum/UpdatePost";
	}
	
	@PostMapping("/forum/updated/{postNo}")
	public String updatePost(@ModelAttribute("post") Post post,Model model) {
		post.setMemberNo(1);
		post.setPostDate(new Date());
		
		postService.updatePost(post);
		
		model.addAttribute("result", "修改成功");
		model.addAttribute("post", post);
		return "redirect:/forum/showallmanager";
	}
	
	// 修改留言
	@PostMapping("/UpdatePostComment")
	    public String updatePostComment(
	    		@RequestParam("postNo") Integer postNo,
	    		@RequestParam("postCommentNo") Integer postCommentNo,
	    		@RequestParam("updatedPostComment") String updatedPostComment,
	    		@ModelAttribute("postComment")PostComment postComment) {
	    	
	    	int memberNo = 1;
	        Date postCommentDate = new Date();
	        
	        Post post = postService.getPostbyNo(postNo);
	        
	        postComment.setPost(post);
	        postComment.setMemberNo(memberNo);
	        postComment.setPostCommentNo(postCommentNo);
	        postComment.setPostComment(updatedPostComment);
	        postComment.setPostCommentDate(postCommentDate);
	        
	        postcommentService.updateComment(postComment);

	        return "redirect:/forum/showpostbyno/" + postNo;
	    }
}
