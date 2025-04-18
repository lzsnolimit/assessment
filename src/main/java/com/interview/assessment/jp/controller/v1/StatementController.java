package com.interview.assessment.jp.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Statement Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Statements", description = "Statement related APIs")
public class StatementController {

        /**
         * Download monthly statement
         *
         * @param accountId Account ID
         * @param year      Year
         * @param month     Month
         * @return Statement PDF
         */
        @GetMapping("/accounts/{accountId}/statements/{year}/{month}")
        @Operation(summary = "Download monthly statement", description = "Downloads the statement for the specified account and month")
        public ResponseEntity<ByteArrayResource> downloadStatement(
                        @PathVariable Long accountId,
                        @PathVariable Integer year,
                        @PathVariable Integer month) {

                // Currently returns a mock PDF file
                // Will implement real functionality in Task 2 and Task 3
                byte[] data = "Mock PDF content, will be implemented in subsequent tasks".getBytes();
                ByteArrayResource resource = new ByteArrayResource(data);

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=statement-" +
                                accountId + "-" + year + "-" + month + ".pdf");

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_PDF)
                                .contentLength(data.length)
                                .body(resource);
        }
}