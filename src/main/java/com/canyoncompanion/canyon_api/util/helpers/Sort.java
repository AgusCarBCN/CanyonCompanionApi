package com.canyoncompanion.canyon_api.util.helpers;


import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.Set;

public final class Sort {
    public static final int PAGE_SIZE=5;
    private Sort() {} // evita instanciación



    private static Set<String> allowedUserFields = Set.of(
            "name",
            "location",
            "province",
            "verticalCharacter",
            "aquaticCharacter",
            "Commitment",
            "createdAt",
            "updatedAt",
            "status"
    );

    public static org.springframework.data.domain.Sort getSort(String field,
                                                               Boolean desc,
                                                               Set<String> allowedFields) {
        if (allowedFields.contains(field)) {
            return desc? org.springframework.data.domain.Sort.by(field).descending(): org.springframework.data.domain.Sort.by(field).ascending();
        }else{
             throw new BusinessException(ErrorCode.INVALID_FIELD.name(),
                     ErrorCode.INVALID_FIELD.getDefaultMessage(),
                     HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
