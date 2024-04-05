package com.kopchak.authserver.dto.error;

import java.util.Map;

public record MethodArgumentNotValidExceptionDto(String url, Map<String, String> fieldsErrorDetails) {
}