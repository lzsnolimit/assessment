package com.interview.assessment.jp.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Statement Request DTO
 */
@Data
public class StatementRequest {
    /**
     * Year
     */
    @NotNull(message = "Year cannot be null")
    @Min(value = 2000, message = "Year must be greater than or equal to 2000")
    @Max(value = 2100, message = "Year must be less than or equal to 2100")
    private Integer year;

    /**
     * Month
     */
    @NotNull(message = "Month cannot be null")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;
}