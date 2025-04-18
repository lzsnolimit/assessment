package com.interview.assessment.jp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * User ID
     */
    private Long id;

    /**
     * Username
     */
    private String username;

    /**
     * Password (encrypted)
     */
    private String password;

    /**
     * User Roles
     */
    private List<String> roles;

    /**
     * Created At
     */
    private LocalDateTime createdAt;

    /**
     * Updated At
     */
    private LocalDateTime updatedAt;
}