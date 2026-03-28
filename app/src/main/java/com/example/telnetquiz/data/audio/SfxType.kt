package com.example.telnetquiz.data.audio

import com.example.telnetquiz.R

enum class SfxType(val resId: Int) {
    QUESTION_RIGHT(R.raw.question_right),
    QUESTION_WRONG(R.raw.question_wrong),
    QUESTION_REMEDIAL_RIGHT(R.raw.question_remedial_right),
    RESULT_SUCCESS(R.raw.result_success),
    RESULT_FAIL(R.raw.result_fail),
    START_LEVEL(R.raw.start_level),
    PRETEST_SUBMIT(R.raw.pretest_submit),
    PRETEST_RESULT(R.raw.pretest_result)
}
