package uz.snow.clinic.referrer.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.snow.clinic.referrer.model.dto.ReferrerResponse;
import uz.snow.clinic.referrer.model.dto.RegisterReferrerRequest;
import uz.snow.clinic.referrer.model.dto.UpdateReferrerRequest;
import uz.snow.clinic.referrer.model.entity.Referrer;
import uz.snow.clinic.referrer.service.ReferrerService;
import uz.snow.clinic.user.model.dto.response.ApiResponse;

import java.util.Comparator;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/referrers")
public class ReferrerController {
    private final ReferrerService referrerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReferrerResponse>>> findAll() {
        List<ReferrerResponse> referrers = referrerService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Referrers fethced successfully ", referrers));
    }

    // GET /api/v1/referrers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReferrerResponse>> findById(@PathVariable Long id) {
        ReferrerResponse referrer = referrerService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Referrers fethced successfully ", referrer));
    }

    // GET /api/v1/referrers/code/{uniqueCode}
    // Useful endpoint — look up referrer by their unique code
    @GetMapping("/code/{uniqueCode}")
    public ResponseEntity<ApiResponse<ReferrerResponse>> findByUniqueCode(
            @PathVariable String uniqueCode) {
        ReferrerResponse referrer = referrerService.findByUniqueCode(uniqueCode);
        return ResponseEntity.ok(ApiResponse.success("Referrers fethced successfully ", referrer));
    }

    // POST /api/v1/referrers
    @PostMapping
    public ResponseEntity<ApiResponse<ReferrerResponse>> register(
            @Valid @RequestBody RegisterReferrerRequest registerReferrerRequest) {

        ReferrerResponse saved = referrerService.register(registerReferrerRequest);

        // 201 Created — correct HTTP status for resource creation
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Referrer registered  successfully ", saved));
    }

    // PUT /api/v1/referrers
    @PutMapping
    public ResponseEntity<ApiResponse<ReferrerResponse>> update(
            @Valid @RequestBody UpdateReferrerRequest request) {
        ReferrerResponse updated = referrerService.update(request);

        return ResponseEntity.ok(ApiResponse.success(
                "Referrer updated successfully ", updated));

    }

    // DELETE /api/v1/referrers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>>delete(@PathVariable Long id) {
        referrerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
