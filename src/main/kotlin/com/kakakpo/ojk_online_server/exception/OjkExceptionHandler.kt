package com.kakakpo.ojk_online_server.exception

import com.kakakpo.ojk_online_server.base.entity.BaseResponse
import com.kakakpo.ojk_online_server.base.entity.Empty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class OjkExceptionHandler {

    @ExceptionHandler(value = [OjkException::class])
    fun handleThrowable(throwable: OjkException): ResponseEntity<BaseResponse<Empty>> {
        return ResponseEntity(BaseResponse.failure(throwable.message ?: "Failure"), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}