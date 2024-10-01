package com.example.meettify.dto.item.status;

import io.swagger.v3.oas.annotations.media.Schema;
/*
 *   writer  : 유요한
 *   work    : 상품 상태를 나타내는 클래스
 *   date    : 2024/09/30
 * */
public enum ItemStatus {
    @Schema(description = "판매중")
    // 판매중
    SELL,
    @Schema(description = "검토중")
    // 검토중
    WAIT,
    @Schema(description = "판매 완료")
    // 판매 완료
    SOLD_OUT
}
