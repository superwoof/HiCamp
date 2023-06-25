package tw.hicamp.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import tw.hicamp.member.model.Manager;
import tw.hicamp.member.model.Member;
import tw.hicamp.member.service.ManagerService;
import tw.hicamp.member.service.MemberService;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
//	登入判斷
	@PostMapping("/login")
	public String login(@RequestParam("memberEmail")String email,
						@RequestParam("memberPassword")String password,
						HttpSession session,
						Model m) {
		Member member = memberService.findByEmail(email);
		Manager manager = managerService.findbyaccount(email);
		
		if (manager != null) {
			//管理員登入成功
			if (password.equals(manager.getManagerPassword())) {
				session.setAttribute("name", manager.getManagerName());
				session.setAttribute("managerAccount", manager.getManagerName());
				System.out.println("管理員登入成功");
				return "redirect:/testsearchallmember";
			} else {
				m.addAttribute("passwordFalse", "密碼錯誤");
				return "member/login";
			}
		} else if (member != null) {
			//會員登入成功
			if (member.getMemberStatus() != 1) {
				m.addAttribute("statusError", "狀態異常");
				return "member/login";
			} else {
				if (passwordEncoder.matches(password, member.getMemberPassword())) {
					System.out.println(member.getMemberName());
					session.setAttribute("name", member.getMemberName());
					session.setAttribute("memberNo", member.getMemberNo());
					System.out.println("會員登入成功");
					return "member/newIndex";
				} else {
					m.addAttribute("passwordFalse", "密碼錯誤");
					return "member/login";
				}
			}
			
		} else {
			m.addAttribute("statusError", "信箱錯誤");
			return "member/login";
		}
	}
	
	//登出會員
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "member/newIndex";
	}
	
	//會員個人資料畫面
	@RequestMapping("projectmembersetting")
	public String projectMemberSettint(HttpSession session,Model m) {
		Member member = memberService.findByNo((int) session.getAttribute("memberNo"));
		m.addAttribute("memberInfo", member);
		return "member/setting";
	}

	//會員修改自己的資料
	@PutMapping("projectmemberupdate")
	@ResponseBody
	public Member ProjectMemberUpdate(HttpSession session,
									  @RequestBody Member member,
									  Model m) {
		int memberNo = (int) session.getAttribute("memberNo");
		Member updateMember = memberService.findByNo(memberNo);
		updateMember.setMemberName(member.getMemberName());
		updateMember.setMemberEmail(member.getMemberEmail());
		updateMember.setMemberPassword(member.getMemberPassword());
		updateMember.setMemberPhone(member.getMemberPhone());
		updateMember.setMemberId(member.getMemberId());
		updateMember.setMemberBirthday(member.getMemberBirthday());
		updateMember.setMemberAddress(member.getMemberAddress());
		updateMember.setMemberEmergencyContact(member.getMemberEmergencyContact());
		updateMember.setMemberEmergencyPhone(member.getMemberEmergencyPhone());
		
		Member updatedMember = memberService.updateMember(updateMember);
		
		m.addAttribute("memberInfo", updatedMember);
		
		return updatedMember;
	}
	
	//會員修改自己的照片
	@ResponseBody
	@PostMapping("/insertphoto")
	public boolean insertPhoto(@RequestParam("memberPhoto")MultipartFile memberPhoto,HttpSession session) {
		return memberService.updatePhoto((int)session.getAttribute("memberNo"), memberPhoto);
	}
	
	//取得照片
	@ResponseBody
	@GetMapping("/getphoto")
	public ResponseEntity<byte[]> getMemberPhoto(@RequestParam("memberNo")int memberNo){
		Member member = memberService.findByNo(memberNo);
		byte[] memberPhoto = member.getMemberPhoto();
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(memberPhoto);
	}
	
	//取得註冊會員資料
	@PostMapping("/getmemberinformation")
	public String getMemberInformation(@RequestParam("memberName") String memberName,
									   @RequestParam("memberGender")String memberGender,
									   @RequestParam("memberEmail")String memberEmail,
									   @RequestParam("memberPassword")String memberPassword,
									   @RequestParam("memberPhone")String memberPhone,
									   @RequestParam("memberAddress") String memberAddress,
									   @RequestParam("memberId") String memberId,
									   @RequestParam("memberBirthday") String memberBirthday,
									   @RequestParam("memberEmergencyContact") String memberEmergencyContact,
									   @RequestParam("memberEmergencyPhone") String memberEmergencyPhone,
									   HttpSession session) {
		Member member = new Member();
		member.setMemberName(memberName);
		member.setMemberGender(memberGender);
		member.setMemberEmail(memberEmail);
		member.setMemberPassword(memberPassword);
		member.setMemberPhone(memberPhone);
		member.setMemberAddress(memberAddress);
		member.setMemberId(memberId);
		member.setMemberBirthday(memberBirthday);
		member.setMemberEmergencyContact(memberEmergencyContact);
		member.setMemberEmergencyPhone(memberEmergencyPhone);
		member.setMemberStatus(1);
		
		memberService.createMember(member);
		
		session.setAttribute("name", member.getMemberName());//顯示在網頁用
		session.setAttribute("memberNo", member.getMemberNo());//查詢資料用
		
		return "member/newIndex";
	}
	
	//註冊時判斷信箱是否重複
	@GetMapping("checkemail")
	@ResponseBody
	public Boolean checkEmail(@RequestParam String email) {
		Member findByEmail = memberService.findByEmail(email);
		if (findByEmail != null) {
			return false;
		}
		return true;
	}
	
	//------------------------------------------------------
	
	//取得所有會員資料
	@GetMapping("testsearchallmember")
//	@ResponseBody
	public String searchAllMember(Model m) {
		List<Member> members = memberService.findAllMember();
		m.addAttribute("members", members);
		return "member/manageMemberPage";
	}
	
	//在管理頁面查詢單筆會員資料
	@GetMapping("searchonemember")
	@ResponseBody
	public Member searchOneMember(@RequestParam("memberNo")Integer no,
								  Model m) {
		Member member = memberService.findByNo(no);
//		m.addAttribute("member", member);
		
		return member;
	}
	
	//管理頁面修改會員資料
	@PutMapping("projectmanagememberupdate")
	@ResponseBody
	public Member projectManageMemberUpdate(@RequestBody Member member) {
		System.out.println("更新誰的資料?"+ member.getMemberName());
		System.out.println();
		Member updatedMember = memberService.updateMember(member);
		
		return updatedMember;
	}
	
	//會員修改自己的照片
		@ResponseBody
		@PostMapping("/managerinsertphoto")
		public boolean managerUpdatePhoto(@RequestParam("memberPhoto")MultipartFile memberPhoto,@RequestParam("memberNo") int memberNo) {
			return memberService.updatePhoto(memberNo, memberPhoto);
		}
	
}
