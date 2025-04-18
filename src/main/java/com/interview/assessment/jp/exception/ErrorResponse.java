package com.interview.assessment.jp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Error Response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * HTTP Status Code
     */
    private int status;

    /**
     * Error Message
     */
    private String message;

    /**
     * Timestamp
     */
    private LocalDateTime timestamp;
}