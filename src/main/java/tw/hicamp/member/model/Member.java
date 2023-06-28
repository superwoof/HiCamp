package tw.hicamp.member.model;


import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import tw.hicamp.forum.model.Post;
import tw.hicamp.forum.model.PostComment;
import tw.hicamp.forum.model.PostLike;

@Entity
@Table(name = "member")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int memberNo;
	private String memberName;
	private String memberGender;
	private String memberEmail;
	private String memberPassword;
	private String memberPhone;
	private String memberAddress;
	private String memberId;
	private String memberBirthday;
	private String memberEmergencyContact;
	private String memberEmergencyPhone;
	private int memberStatus;
	private byte[] memberPhoto;
	
	@JsonIgnore
	@JsonIgnoreProperties("member")
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "member")
	private Set<Post> posts = new HashSet<>();
	
	@JsonIgnore
	@JsonIgnoreProperties("member")
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "member")
	private Set<PostComment> postcomments = new HashSet<>();
	
	@JsonIgnore
	@JsonIgnoreProperties("member")
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "member")
	private Set<PostLike> postlikes = new HashSet<>();
}
