package org.gdglille.devfest.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class QuestionAndResponseUi(
    val question: String,
    val response: String,
    val actions: ImmutableList<QuestionAndResponseActionUi>,
    val expanded: Boolean = false
) {
    companion object {
        val fake = QuestionAndResponseUi(
            question = "Comment devenir partenaires ?",
            response = "Consultez tout d'abord notre dossier de partenariat, puis remplir ce formulaire.",
            actions = persistentListOf(
                QuestionAndResponseActionUi(
                    label = "Dossier Partenariat",
                    url = "https://devfest.gdglille.org/partenaire.pdf"
                ),
                QuestionAndResponseActionUi(
                    label = "CMS4Partners",
                    url = "https://cms4partners.gdglille.org/#/2022"
                )
            )
        )
    }
}

data class QuestionAndResponseActionUi(
    val label: String,
    val url: String
)
