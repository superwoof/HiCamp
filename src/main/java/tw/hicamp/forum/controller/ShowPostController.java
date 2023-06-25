package tw.hicamp.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import tw.hicamp.forum.model.Post;
import tw.hicamp.forum.model.PostComment;
import tw.hicamp.forum.service.PostCommentService;
import tw.hicamp.forum.service.PostService;


@Controller
public class ShowPostController{
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private PostCommentService postcommentService;
	
	// 查全部貼文 (管理者頁面)
	@GetMapping("/forum/showallmanager")
    public String getAllPostMain(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "/forum/ManagerHomepage";
    }
	
	// 查全部貼文 (使用者頁面) 
	@GetMapping("/forum/showall")
	public String showAllPostMain(Model model) {
		List<Post> posts = postService.getAllPosts();
		model.addAttribute("posts", posts);
		return "/forum/UserHomepage";
	}
   
	// 查單一貼文
	@GetMapping("/forum/showpostbyno/{postNo}")
	public String getForumPostDetail1(@PathVariable("postNo") Integer postNo, Model model) {
		Post post = postService.getPostbyNo(postNo);
		List<PostComment> comments = postcommentService.getCommentsByPostSortedByNo(post);
		
		model.addAttribute("post", post);
		model.addAttribute("comments", comments);
		
		return "/forum/PostContentByNo";
	}
}
