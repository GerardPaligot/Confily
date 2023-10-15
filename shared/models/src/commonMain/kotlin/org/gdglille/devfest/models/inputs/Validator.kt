package org.gdglille.devfest.models.inputs

class ValidatorException(val errors: List<String>) : Throwable()

interface Validator {
    fun validate(): List<String>
}
