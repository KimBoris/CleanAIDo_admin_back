package org.zerock.cleanaido_admin_back.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    @Min(value = 1, message = "over 1")
    private int page = 1;

    @Builder.Default
    @Min(value = 10, message = "set over 10")
    @Max(value = 100, message = "cannot over 100")
    private int size = 10;

    private SearchDTO searchDTO;

    // 페이지와 사이즈만 받는 생성자 추가
    public PageRequestDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    // Pageable 객체 생성 메서드
    public Pageable toPageable() {
        return PageRequest.of(this.page - 1, this.size, Sort.by(Sort.Direction.DESC, "orderNumber"));
    }
}
