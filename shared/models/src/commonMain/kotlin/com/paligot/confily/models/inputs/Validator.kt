package com.paligot.confily.models.inputs

class ValidatorException(val errors: List<String>) : Throwable() {
    constructor(message: String) : this(listOf(message))
}

interface Validator {
    fun validate(): List<String>
}
