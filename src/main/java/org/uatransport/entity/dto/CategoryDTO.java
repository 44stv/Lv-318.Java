package org.uatransport.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.uatransport.entity.ExtendableCategory;

@Data
@AllArgsConstructor
public class CategoryDTO {
    Integer id;
    String name;
    ExtendableCategory nextLevelCategory;
    String iconURL;
    Integer countOfTransits;
    Double rating;
}
