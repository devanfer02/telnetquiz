package com.example.litecartesnative.features.pretest.presentation.singletons

import com.example.litecartesnative.data.remote.dto.PretestResultDto

object PretestResultHolder {
    var lastResult: PretestResultDto? = null

    fun clear() {
        lastResult = null
    }
}
