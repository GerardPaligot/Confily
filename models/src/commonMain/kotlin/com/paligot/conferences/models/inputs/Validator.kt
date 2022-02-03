package com.paligot.conferences.models.inputs

class ValidatorException(val errors: List<String>) : Throwable()


interface Validator {
    fun validate(): List<String>
}