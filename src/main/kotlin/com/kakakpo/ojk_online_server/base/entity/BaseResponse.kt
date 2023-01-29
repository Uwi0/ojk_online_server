package com.kakakpo.ojk_online_server.base.entity

open class BaseResponse<T>(
        var status: Boolean = true,
        var message: String = "success",
        val data: T? = null
){
    companion object{
        fun <T>success(data: T?): BaseResponse<T>{
            return BaseResponse(data = data)
        }

        fun <T>failure(message: String): BaseResponse<T>{
            return BaseResponse(status = false, message = message)
        }
    }
}