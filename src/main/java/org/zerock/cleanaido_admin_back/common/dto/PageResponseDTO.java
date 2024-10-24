package org.zerock.cleanaido_admin_back.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> {
    private List<E> dtoList;
    private List<Integer> pageNumList;
    private PageRequestDTO pageRequestDTO;
    private boolean prev, next;
    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int) totalCount;

        int totalPage = (int) Math.ceil((double) totalCount / pageRequestDTO.getSize());

        int end = (int) (Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;
        int start = end - 9;


        end =Math.min(end, totalPage);

        this.prev = start > 1;
        this.next = end < totalPage;

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        if (prev)
            this.prevPage = start - 1;
        if (next)
            this.nextPage = end + 1;
        this.totalPage = totalPage;
        this.current = pageRequestDTO.getPage();
    }
}