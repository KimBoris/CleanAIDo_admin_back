package org.zerock.cleanaido_admin_back.product.controller;


import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.zerock.cleanaido_admin_back.common.dto.PageRequestDTO;
import org.zerock.cleanaido_admin_back.common.dto.PageResponseDTO;
import org.zerock.cleanaido_admin_back.product.dto.ReviewListDTO;
import org.zerock.cleanaido_admin_back.product.service.ReviewService;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
@ToString
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175"})
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<ReviewListDTO>> list(@RequestParam(value = "page", defaultValue = "1") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        String customerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().
                next().getAuthority();

        log.info("UserId = " + customerId);
        log.info("Role = " + role);


        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .build();


        return ResponseEntity.ok(reviewService.list(pageRequestDTO,  role));
    }

}
