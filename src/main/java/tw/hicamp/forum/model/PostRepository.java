package tw.hicamp.forum.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Integer> {

	List<Post> findAllByOrderByPostNoDesc();
	
	List<Post> findByPostTypeOrderByPostNoDesc(String postType);
	
	List<Post> findByPostTitleContainingOrderByPostNoDesc(String keyword);

	

}
