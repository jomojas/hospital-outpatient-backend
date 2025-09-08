package com.ncst.hospitaloutpatient.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * 统一响应模型：
 * - 成功：code=200, message="OK", data!=null(可为null也会输出), meta(仅分页接口返回)
 * - 失败：code!=0, message!=null, data省略(始终为null且不输出), meta不返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // 类级别：为 null 的字段不序列化
public class ApiResponse<T> {

    private int code;          // 200 表示成功，非 200 为错误
    private String message;    // 人类可读信息

    // data 字段始终序列化（即便为 null），满足“所有成功响应都有 data 字段”的约定
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    private T data;            // 业务载荷

    private Meta meta;         // 元信息（仅分页接口返回；非分页接口不返回）

    // ====== 成功工厂（默认不返回 meta） ======

    /**
     * 仅操作成功，无专门数据载荷。
     * 输出：{ "code":200, "message":"OK", "data": null }
     */
    public static <T> ApiResponse<T> ok() {
        return ApiResponse.<T>builder()
                .code(200)
                .message("OK")
                .data(null) // 在字段上强制 ALWAYS 序列化 -> "data": null
                .build();
    }

    /**
     * 携带数据的成功返回（非分页）。
     * 输出：{ "code":200, "message":"OK", "data": { ... } }
     */
    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("OK")
                .data(data)
                .build();
    }

    /**
     * 分页成功返回。自动计算 totalPages，并仅在分页接口返回 meta。
     * 输出：{ "code":200, "message":"OK", "data":{...}, "meta":{page,size,total,totalPages} }
     */
    public static <T> ApiResponse<T> pageOk(T data, int page, int size, long total) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("OK")
                .data(data)
                .meta(Meta.page(page, size, total))
                .build();
    }

    // ====== 失败工厂（仅返回 code 与 message） ======

    /**
     * 失败返回。按约定不返回 meta，也不返回 data。
     * 输出：{ "code":xxx, "message":"..." }
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                // data=null 且类级别 NON_NULL -> 不输出 data 字段
                // meta=null -> 不输出 meta 字段
                .build();
    }

    // ====== 元信息对象（仅分页字段） ======
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Meta {
        private Integer page;       // 当前页
        private Integer size;       // 页长
        private Long total;         // 总数
        private Integer totalPages; // 总页数

        public static Meta page(int page, int size, long total) {
            int totalPages = (size > 0) ? (int) Math.ceil((double) total / (double) size) : 0;
            return Meta.builder()
                    .page(page)
                    .size(size)
                    .total(total)
                    .totalPages(totalPages)
                    .build();
        }
    }
}