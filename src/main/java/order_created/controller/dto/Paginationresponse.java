package order_created.controller.dto;

import org.springframework.data.domain.Page;

public record Paginationresponse(Integer page, Integer pageSize, Long totalElements, Integer totalPages) {

    public static Paginationresponse fromPage(Page page){
        return new Paginationresponse(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
