package com.sivalabs.bookmarks.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Setter
@Getter
@AllArgsConstructor
public class PagedResult<T> {
    private Pageable pageable;
    private List<T> data;
    private long totalElements;
    private int pageNumber;
    private int totalPages;

    @JsonProperty("isFirst")
    private boolean isFirst;

    @JsonProperty("isLast")
    private boolean isLast;

    @JsonProperty("hasNext")
    private boolean hasNext;

    @JsonProperty("hasPrevious")
    private boolean hasPrevious;

    public PagedResult(Page<T> page) {
        this.setPageable(page.getPageable());
        this.setData(page.getContent());
        this.setTotalElements(page.getTotalElements());
        this.setPageNumber(page.getNumber() + 1); // 1 - based page numbering
        this.setTotalPages(page.getTotalPages());
        this.setFirst(page.isFirst());
        this.setLast(page.isLast());
        this.setHasNext(page.hasNext());
        this.setHasPrevious(page.hasPrevious());
    }
}
