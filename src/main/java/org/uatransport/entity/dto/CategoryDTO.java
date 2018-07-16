package org.uatransport.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.uatransport.entity.ExtendableCategory;

@Data
@AllArgsConstructor
public class CategoryDTO {
    private Integer id;
    private String name;
    private ExtendableCategory nextLevelCategory;
    private String iconURL;
    private Integer countOfTransits;
}
