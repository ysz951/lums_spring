package contest.controller;



//import contest.model.;
import contest.data.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import contest.model.Member;
import contest.model.Member.Role;

import javax.xml.bind.ValidationException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/members")
public class MemberController {

    private MemberRepository memberRepository;

    @Autowired
    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        memberRepository.createMember(member);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> memberList = memberRepository.getAllMembers();
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable("id") long id) {
        try {
            Member member = memberRepository.getMemberById(id);
            return new ResponseEntity<>(member, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("no member found", HttpStatus.BAD_REQUEST);
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("no member found");
        }

    }

    @PostMapping("/{id}/modify_role")
    public ResponseEntity<?> changeRole(@PathVariable("id") long id,
                                        @RequestParam("adminId") long adminId,
                                        @RequestParam("newRole") Role newRole) {
        try {
            memberRepository.modifyRole(id, newRole, adminId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("no member found");
        }
    }

    @PostMapping("/password/{id}/{oldPassword}/{newPassword}")
    public ResponseEntity<?> changePassword(@PathVariable("id") long id,
                                            @PathVariable("oldPassword") String oldPassword,
                                            @PathVariable("newPassword") String newPassword) {
        try {
            Member member = memberRepository.getMemberById(id);
            if (!checkPasswordCredintials(member, oldPassword)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("password is invalid");
            }
            member.setPassword(newPassword);
            memberRepository.updateMember(member);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("no member found");
        }
    }

    @PostMapping("/block/{id}")
    public ResponseEntity<?> block(@PathVariable("id") long id,
                                   @RequestParam("adminId") long adminId) {
        try {
            memberRepository.block(id, adminId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("no member found");
        }
    }

    @PostMapping("/unblock/{id}")
    public ResponseEntity<?> unblock(@PathVariable("id") long id,
                                   @RequestParam("adminId") long adminId) {
        try {
            memberRepository.unblock(id, adminId);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("no member found");
        }
    }

    @PutMapping("/email/{id}/{newEmail}")
    public ResponseEntity<?> updateMemberEmail(@PathVariable("id") long id,
                                               @PathVariable("newEmail") String newEmail ) {
        try {
            Member member = memberRepository.getMemberById(id);
            validateMemberNewEmail(member, newEmail);
            member.setEmail(newEmail);
            memberRepository.updateMember(member);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        catch (ValidationException ve) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ve.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("no member found");
        }
    }

    public boolean checkPasswordCredintials(Member member, String password) {
        return member.getPassword().compareTo(password) == 0;
    }

    private void validateMemberNewEmail(Member member, String email) throws ValidationException {

        List<Member> memberList = memberRepository.findAllMemberByEmail(email);
        if (memberList.size() == 1) {
            Member cur = memberList.get(0);
            if (!(cur.getId().equals(member.getId()))) {
                throw new ValidationException("Invalid email");
            }
        }
    }
}
