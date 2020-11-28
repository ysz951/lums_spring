package contest.controller;

import contest.data.LicenseRepository;
import contest.data.MemberRepository;
import contest.model.License;
import contest.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/license")
public class LicenseController {

    private LicenseRepository licenseRepository;

    @Autowired
    public LicenseController(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    @GetMapping
    public ResponseEntity<List<License>> getAllLicenses() {
        List<License> licenseList = licenseRepository.findAllOrderedById();
        return new ResponseEntity<>(licenseList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License license) {
        licenseRepository.save(license);
        return new ResponseEntity<>(license, HttpStatus.OK);
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<?> purchaseLicense(@PathVariable("id") long id,
                                             @RequestParam("userId") long userId){
        try {
            licenseRepository.purchase(id, userId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("no member found");
        }

    }

}
