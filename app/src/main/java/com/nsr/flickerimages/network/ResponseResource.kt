package com.nsr.flickerimages.network

import com.nsr.flickerimages.R

data class ResponseResource<out T>(val status: Status, val data: T? = null, val message: Int = R.string.error_message) {
    companion object {
        fun <T> success(data: T): ResponseResource<T> = ResponseResource(status = Status.SUCCESS, data = data)

        fun <T> error(message: Int): ResponseResource<T> =
            ResponseResource(status = Status.ERROR, message = message)

        fun <T> loading(): ResponseResource<T> = ResponseResource(status = Status.LOADING)

        fun <T> init(): ResponseResource<T> = ResponseResource(status = Status.INIT)
    }
}