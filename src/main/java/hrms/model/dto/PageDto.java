package hrms.model.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PageDto<T> {

    // 1. 반환된 Dto List
    List<T> someList;
    // 2. 반환된 총 페이지수
    int totalPages;
    // 3. 반환된 총 요소들수
    long totalCount;

}
