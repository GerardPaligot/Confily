package org.gdglille.devfest.android.ui.resources

sealed class ActionItemId {
    object FavoriteSchedulesActionItem : ActionItemId()
    object VCardQrCodeScannerActionItem : ActionItemId()
    object QrCodeActionItem : ActionItemId()
    object ShareActionItem : ActionItemId()
    object ReportActionItem : ActionItemId()
}
