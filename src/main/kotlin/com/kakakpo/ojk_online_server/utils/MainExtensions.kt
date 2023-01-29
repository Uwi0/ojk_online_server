package com.kakakpo.ojk_online_server.utils

import com.kakakpo.ojk_online_server.base.entity.BaseResponse
import com.kakakpo.ojk_online_server.exception.OjkException

inline fun <reified T> T?.orThrow(message: String = "${T::class.simpleName}"): T {
    return this ?: throw OjkException(message)
}


inline fun <reified T> T?.toResult(message: String = "${T::class.simpleName} is null"): Result<T> {
    return if (this != null) {
        Result.success(this)
    } else {
        Result.failure(OjkException(message))
    }
}

fun <T>Result<T>.toResponse(): BaseResponse<T>{
    return if (this.isFailure){
        throw OjkException(this.exceptionOrNull()?.message ?: "")
    }else{
        BaseResponse.success(this.getOrNull())
    }
}
