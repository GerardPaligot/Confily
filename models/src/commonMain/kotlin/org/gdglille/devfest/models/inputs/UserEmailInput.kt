package org.gdglille.devfest.models.inputs

import kotlinx.serialization.Serializable

@Serializable
data class UserEmailInput(
    val email: String
) : Validator {
    override fun validate(): List<String> {
        val errors = arrayListOf<String>()
        val pattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        if (!email.matches(Regex(pattern))) errors.add("Your email address is not well formatted")
        return errors
    }
}
