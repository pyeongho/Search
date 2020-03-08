package com.arch.search.entity


sealed class ResultSearch {
    class CommonError(val data: String) : ResultSearch()
    class ApiError(val data: ApiErrorMessage) : ResultSearch()
    class Success(val data: Searchs) : ResultSearch()
    class ConnectionError(val data: String) : ResultSearch()
}
