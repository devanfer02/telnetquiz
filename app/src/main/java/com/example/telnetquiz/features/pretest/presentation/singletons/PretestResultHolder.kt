package com.example.telnetquiz.features.pretest.presentation.singletons

import com.example.telnetquiz.data.remote.dto.PretestResultDto

object PretestResultHolder {
    var lastResult: PretestResultDto? = null

    fun clear() {
        lastResult = null
    }
}
