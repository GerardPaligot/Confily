package com.paligot.confily.navigation

sealed class Screen(val route: String) {
    data object EventList : Screen(route = "events")
    data object FutureEvents : Screen(route = "events/future")
    data object PastEvents : Screen(route = "events/past")
    data object ScheduleList : Screen(route = "schedules")
    data object ScheduleFilters : Screen(route = "schedules/filters")
    data object Schedule : Screen(route = "schedules/{scheduleId}") {
        fun route(scheduleId: String) = "schedules/$scheduleId"
    }
    data object ScheduleEvent : Screen(route = "schedules/{scheduleId}/event") {
        fun route(scheduleId: String) = "schedules/$scheduleId/event"
    }

    data object SpeakerList : Screen(route = "speakers")
    data object Speaker : Screen(route = "speakers/{speakerId}") {
        fun route(speakerId: String) = "speakers/$speakerId"
    }

    data object MyProfile : Screen(route = "profile/me")
    data object NewProfile : Screen(route = "profile/new")
    data object Contacts : Screen(route = "contacts")
    data object PartnerList : Screen(route = "partners")
    data object Partner : Screen(route = "partners/{partnerId}") {
        fun route(partnerId: String) = "partners/$partnerId"
    }

    data object Event : Screen(route = "event")
    data object Menus : Screen(route = "menus")
    data object QAndA : Screen(route = "qanda")
    data object CoC : Screen(route = "coc")
    data object ScannerVCard : Screen(route = "scanner/vcard")
    data object ScannerTicket : Screen(route = "scanner/ticket")
}
