package org.gdglille.devfest.models

import org.gdglille.devfest.Image

data class TicketUi(
  val id: String,
  val firstName: String,
  val lastName: String,
  val qrCode: Image?
) {
  companion object {
    val fake = TicketUi(
      id = "T264-8780-E519953",
      firstName = "Gérard",
      lastName = "Paligot",
      qrCode = null
    )
    val fakeLongText = TicketUi(
      id = "T264-8780-E519953",
      firstName = "Gérard Pierre Martin Adrien",
      lastName = "Paligot",
      qrCode = null
    )
  }
}
