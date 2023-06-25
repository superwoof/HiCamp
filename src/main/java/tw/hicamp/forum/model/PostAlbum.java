package tw.hicamp.forum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "postAlbum")
public class PostAlbum{
	
	@Id
	@Column(name = "postAlbumNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String postAlbumNo;
	
	@Column(name = "postNo")
	private String postNo;
	
	@Column(name = "postAlbum")
	private String postAlbum;
	
	
}
