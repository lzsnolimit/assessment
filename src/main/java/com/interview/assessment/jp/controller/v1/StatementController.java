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
         * @return Statement TXT
         */
        @GetMapping("/accounts/{accountId}/statements/{year}/{month}")
        @Operation(summary = "Download monthly statement", description = "Downloads the statement for the specified account and month")
        public ResponseEntity<ByteArrayResource> downloadStatement(
                        @PathVariable Long accountId,
                        @PathVariable Integer year,
                        @PathVariable Integer month) {

                // 生成简单的文本格式对账单
                StringBuilder statementBuilder = new StringBuilder();
                statementBuilder.append("账户对账单\n");
                statementBuilder.append("========================\n");
                statementBuilder.append("账户ID: ").append(accountId).append("\n");
                statementBuilder.append("对账期间: ").append(year).append("年").append(month).append("月\n");
                statementBuilder.append("========================\n\n");
                statementBuilder.append("交易明细:\n");
                statementBuilder.append("1. 2023-01-01 - 入账 - ¥1000.00 - 工资入账\n");
                statementBuilder.append("2. 2023-01-05 - 出账 - ¥200.00 - 超市购物\n");
                statementBuilder.append("3. 2023-01-15 - 出账 - ¥500.00 - 房租\n");
                statementBuilder.append("========================\n");
                statementBuilder.append("期末余额: ¥8000.00\n");

                byte[] data = statementBuilder.toString().getBytes();
                ByteArrayResource resource = new ByteArrayResource(data);

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=statement-" +
                                accountId + "-" + year + "-" + month + ".txt");

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.TEXT_PLAIN)
                                .contentLength(data.length)
                                .body(resource);
        }
}