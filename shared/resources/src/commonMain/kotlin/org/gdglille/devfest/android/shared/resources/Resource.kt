package org.gdglille.devfest.android.shared.resources

import conferences4hall.shared.resources.generated.resources.Res
import conferences4hall.shared.resources.generated.resources.action_back
import conferences4hall.shared.resources.generated.resources.action_contact_organizers_mail
import conferences4hall.shared.resources.generated.resources.action_contact_organizers_phone
import conferences4hall.shared.resources.generated.resources.action_contacts_scanner
import conferences4hall.shared.resources.generated.resources.action_edit_profile
import conferences4hall.shared.resources.generated.resources.action_expanded_menu
import conferences4hall.shared.resources.generated.resources.action_export
import conferences4hall.shared.resources.generated.resources.action_favorites_add
import conferences4hall.shared.resources.generated.resources.action_favorites_remove
import conferences4hall.shared.resources.generated.resources.action_filtering
import conferences4hall.shared.resources.generated.resources.action_filtering_favorites
import conferences4hall.shared.resources.generated.resources.action_generate_qrcode
import conferences4hall.shared.resources.generated.resources.action_networking_delete
import conferences4hall.shared.resources.generated.resources.action_power_off
import conferences4hall.shared.resources.generated.resources.action_qrcode_scanner
import conferences4hall.shared.resources.generated.resources.action_share_talk
import conferences4hall.shared.resources.generated.resources.action_submit_accept
import conferences4hall.shared.resources.generated.resources.action_submit_deny
import conferences4hall.shared.resources.generated.resources.action_submit_later
import conferences4hall.shared.resources.generated.resources.action_system_settings
import conferences4hall.shared.resources.generated.resources.action_ticket_scanner
import conferences4hall.shared.resources.generated.resources.input_company
import conferences4hall.shared.resources.generated.resources.input_email
import conferences4hall.shared.resources.generated.resources.input_firstname
import conferences4hall.shared.resources.generated.resources.input_lastname
import conferences4hall.shared.resources.generated.resources.input_share_talk
import conferences4hall.shared.resources.generated.resources.screen_agenda
import conferences4hall.shared.resources.generated.resources.screen_agenda_filters
import conferences4hall.shared.resources.generated.resources.screen_coc
import conferences4hall.shared.resources.generated.resources.screen_contacts
import conferences4hall.shared.resources.generated.resources.screen_event
import conferences4hall.shared.resources.generated.resources.screen_events
import conferences4hall.shared.resources.generated.resources.screen_events_future
import conferences4hall.shared.resources.generated.resources.screen_events_past
import conferences4hall.shared.resources.generated.resources.screen_info
import conferences4hall.shared.resources.generated.resources.screen_menus
import conferences4hall.shared.resources.generated.resources.screen_my_profile
import conferences4hall.shared.resources.generated.resources.screen_networking
import conferences4hall.shared.resources.generated.resources.screen_partners
import conferences4hall.shared.resources.generated.resources.screen_partners_detail
import conferences4hall.shared.resources.generated.resources.screen_profile
import conferences4hall.shared.resources.generated.resources.screen_qanda
import conferences4hall.shared.resources.generated.resources.screen_qrcode_scanner
import conferences4hall.shared.resources.generated.resources.screen_schedule_detail
import conferences4hall.shared.resources.generated.resources.screen_schedule_detail_event_session
import conferences4hall.shared.resources.generated.resources.screen_speaker_detail
import conferences4hall.shared.resources.generated.resources.screen_speakers
import conferences4hall.shared.resources.generated.resources.screen_ticket_qrcode_scanner
import conferences4hall.shared.resources.generated.resources.semantic_github
import conferences4hall.shared.resources.generated.resources.semantic_linkedin
import conferences4hall.shared.resources.generated.resources.semantic_mastodon
import conferences4hall.shared.resources.generated.resources.semantic_pause_item
import conferences4hall.shared.resources.generated.resources.semantic_profile_qrcode
import conferences4hall.shared.resources.generated.resources.semantic_start_itinerary
import conferences4hall.shared.resources.generated.resources.semantic_talk_item
import conferences4hall.shared.resources.generated.resources.semantic_talk_item_level
import conferences4hall.shared.resources.generated.resources.semantic_talk_item_speakers
import conferences4hall.shared.resources.generated.resources.semantic_ticket_id
import conferences4hall.shared.resources.generated.resources.semantic_ticket_qrcode
import conferences4hall.shared.resources.generated.resources.semantic_twitter
import conferences4hall.shared.resources.generated.resources.semantic_user_item_company
import conferences4hall.shared.resources.generated.resources.semantic_user_item_email
import conferences4hall.shared.resources.generated.resources.semantic_website
import conferences4hall.shared.resources.generated.resources.text_agenda_no_favorites
import conferences4hall.shared.resources.generated.resources.text_camera_permission_deny
import conferences4hall.shared.resources.generated.resources.text_camera_permission_explaination
import conferences4hall.shared.resources.generated.resources.text_empty_contacts
import conferences4hall.shared.resources.generated.resources.text_empty_networking
import conferences4hall.shared.resources.generated.resources.text_empty_networking_warning
import conferences4hall.shared.resources.generated.resources.text_error
import conferences4hall.shared.resources.generated.resources.text_export_subject
import conferences4hall.shared.resources.generated.resources.text_feedback_not_configured
import conferences4hall.shared.resources.generated.resources.text_here_we_go
import conferences4hall.shared.resources.generated.resources.text_job_propulsed
import conferences4hall.shared.resources.generated.resources.text_job_requirements_many
import conferences4hall.shared.resources.generated.resources.text_job_requirements_one
import conferences4hall.shared.resources.generated.resources.text_job_salary
import conferences4hall.shared.resources.generated.resources.text_loading
import conferences4hall.shared.resources.generated.resources.text_networking_ask_to_delete
import conferences4hall.shared.resources.generated.resources.text_networking_consents
import conferences4hall.shared.resources.generated.resources.text_openfeedback_not_started
import conferences4hall.shared.resources.generated.resources.text_openfeedback_title
import conferences4hall.shared.resources.generated.resources.text_permission
import conferences4hall.shared.resources.generated.resources.text_report_app_target
import conferences4hall.shared.resources.generated.resources.text_report_subject
import conferences4hall.shared.resources.generated.resources.text_schedule_minutes
import conferences4hall.shared.resources.generated.resources.text_speaker_title
import conferences4hall.shared.resources.generated.resources.text_ticket_firstname
import conferences4hall.shared.resources.generated.resources.text_ticket_lastname
import conferences4hall.shared.resources.generated.resources.title_filters_categories
import conferences4hall.shared.resources.generated.resources.title_filters_favorites
import conferences4hall.shared.resources.generated.resources.title_filters_formats
import conferences4hall.shared.resources.generated.resources.title_jobs
import conferences4hall.shared.resources.generated.resources.title_notif_reminder_talk
import conferences4hall.shared.resources.generated.resources.title_plan
import conferences4hall.shared.resources.generated.resources.title_plan_partner
import conferences4hall.shared.resources.generated.resources.title_schedule_detail
import conferences4hall.shared.resources.generated.resources.title_ticket
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

// FIXME This object class is necessary to expose compose multiplatform resources to others modules.
// https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-images-resources.html
object Resource {
    object string
}

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_back: StringResource
    get() = Res.string.action_back

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_contact_organizers_phone: StringResource
    get() = Res.string.action_contact_organizers_phone

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_contact_organizers_mail: StringResource
    get() = Res.string.action_contact_organizers_mail

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_contacts_scanner: StringResource
    get() = Res.string.action_contacts_scanner

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_edit_profile: StringResource
    get() = Res.string.action_edit_profile

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_export: StringResource
    get() = Res.string.action_export

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_favorites_add: StringResource
    get() = Res.string.action_favorites_add

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_favorites_remove: StringResource
    get() = Res.string.action_favorites_remove

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_filtering: StringResource
    get() = Res.string.action_filtering

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_filtering_favorites: StringResource
    get() = Res.string.action_filtering_favorites

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_generate_qrcode: StringResource
    get() = Res.string.action_generate_qrcode

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_networking_delete: StringResource
    get() = Res.string.action_networking_delete

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_expanded_menu: StringResource
    get() = Res.string.action_expanded_menu

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_power_off: StringResource
    get() = Res.string.action_power_off

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_qrcode_scanner: StringResource
    get() = Res.string.action_qrcode_scanner

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_share_talk: StringResource
    get() = Res.string.action_share_talk

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_submit_accept: StringResource
    get() = Res.string.action_submit_accept

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_submit_deny: StringResource
    get() = Res.string.action_submit_deny

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_submit_later: StringResource
    get() = Res.string.action_submit_later

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_system_settings: StringResource
    get() = Res.string.action_system_settings

@OptIn(ExperimentalResourceApi::class)
val Resource.string.action_ticket_scanner: StringResource
    get() = Res.string.action_ticket_scanner

@OptIn(ExperimentalResourceApi::class)
val Resource.string.input_company: StringResource
    get() = Res.string.input_company

@OptIn(ExperimentalResourceApi::class)
val Resource.string.input_email: StringResource
    get() = Res.string.input_email

@OptIn(ExperimentalResourceApi::class)
val Resource.string.input_firstname: StringResource
    get() = Res.string.input_firstname

@OptIn(ExperimentalResourceApi::class)
val Resource.string.input_lastname: StringResource
    get() = Res.string.input_lastname

@OptIn(ExperimentalResourceApi::class)
val Resource.string.input_share_talk: StringResource
    get() = Res.string.input_share_talk

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_github: StringResource
    get() = Res.string.semantic_github

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_linkedin: StringResource
    get() = Res.string.semantic_linkedin

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_mastodon: StringResource
    get() = Res.string.semantic_mastodon

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_pause_item: StringResource
    get() = Res.string.semantic_pause_item

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_profile_qrcode: StringResource
    get() = Res.string.semantic_profile_qrcode

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_start_itinerary: StringResource
    get() = Res.string.semantic_start_itinerary

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_talk_item: StringResource
    get() = Res.string.semantic_talk_item

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_talk_item_level: StringResource
    get() = Res.string.semantic_talk_item_level

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_talk_item_speakers: StringResource
    get() = Res.string.semantic_talk_item_speakers

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_ticket_id: StringResource
    get() = Res.string.semantic_ticket_id

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_ticket_qrcode: StringResource
    get() = Res.string.semantic_ticket_qrcode

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_twitter: StringResource
    get() = Res.string.semantic_twitter

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_user_item_company: StringResource
    get() = Res.string.semantic_user_item_company

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_user_item_email: StringResource
    get() = Res.string.semantic_user_item_email

@OptIn(ExperimentalResourceApi::class)
val Resource.string.semantic_website: StringResource
    get() = Res.string.semantic_website

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_agenda: StringResource
    get() = Res.string.screen_agenda

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_agenda_filters: StringResource
    get() = Res.string.screen_agenda_filters

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_coc: StringResource
    get() = Res.string.screen_coc

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_contacts: StringResource
    get() = Res.string.screen_contacts

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_event: StringResource
    get() = Res.string.screen_event

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_events: StringResource
    get() = Res.string.screen_events

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_events_future: StringResource
    get() = Res.string.screen_events_future

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_events_past: StringResource
    get() = Res.string.screen_events_past

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_info: StringResource
    get() = Res.string.screen_info

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_menus: StringResource
    get() = Res.string.screen_menus

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_my_profile: StringResource
    get() = Res.string.screen_my_profile

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_networking: StringResource
    get() = Res.string.screen_networking

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_partners: StringResource
    get() = Res.string.screen_partners

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_partners_detail: StringResource
    get() = Res.string.screen_partners_detail

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_profile: StringResource
    get() = Res.string.screen_profile

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_qanda: StringResource
    get() = Res.string.screen_qanda

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_qrcode_scanner: StringResource
    get() = Res.string.screen_qrcode_scanner

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_schedule_detail: StringResource
    get() = Res.string.screen_schedule_detail

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_schedule_detail_event_session: StringResource
    get() = Res.string.screen_schedule_detail_event_session

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_speakers: StringResource
    get() = Res.string.screen_speakers

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_speaker_detail: StringResource
    get() = Res.string.screen_speaker_detail

@OptIn(ExperimentalResourceApi::class)
val Resource.string.screen_ticket_qrcode_scanner: StringResource
    get() = Res.string.screen_ticket_qrcode_scanner

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_agenda_no_favorites: StringResource
    get() = Res.string.text_agenda_no_favorites

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_camera_permission_deny: StringResource
    get() = Res.string.text_camera_permission_deny

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_camera_permission_explaination: StringResource
    get() = Res.string.text_camera_permission_explaination

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_empty_contacts: StringResource
    get() = Res.string.text_empty_contacts

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_empty_networking: StringResource
    get() = Res.string.text_empty_networking

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_empty_networking_warning: StringResource
    get() = Res.string.text_empty_networking_warning

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_networking_consents: StringResource
    get() = Res.string.text_networking_consents

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_error: StringResource
    get() = Res.string.text_error

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_export_subject: StringResource
    get() = Res.string.text_export_subject

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_feedback_not_configured: StringResource
    get() = Res.string.text_feedback_not_configured

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_here_we_go: StringResource
    get() = Res.string.text_here_we_go

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_job_propulsed: StringResource
    get() = Res.string.text_job_propulsed

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_job_requirements_one: StringResource
    get() = Res.string.text_job_requirements_one

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_job_requirements_many: StringResource
    get() = Res.string.text_job_requirements_many

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_job_salary: StringResource
    get() = Res.string.text_job_salary

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_loading: StringResource
    get() = Res.string.text_loading

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_networking_ask_to_delete: StringResource
    get() = Res.string.text_networking_ask_to_delete

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_openfeedback_not_started: StringResource
    get() = Res.string.text_openfeedback_not_started

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_openfeedback_title: StringResource
    get() = Res.string.text_openfeedback_title

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_permission: StringResource
    get() = Res.string.text_permission

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_report_app_target: StringResource
    get() = Res.string.text_report_app_target

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_report_subject: StringResource
    get() = Res.string.text_report_subject

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_schedule_minutes: StringResource
    get() = Res.string.text_schedule_minutes

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_speaker_title: StringResource
    get() = Res.string.text_speaker_title

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_ticket_firstname: StringResource
    get() = Res.string.text_ticket_firstname

@OptIn(ExperimentalResourceApi::class)
val Resource.string.text_ticket_lastname: StringResource
    get() = Res.string.text_ticket_lastname

@OptIn(ExperimentalResourceApi::class)
val Resource.string.title_filters_categories: StringResource
    get() = Res.string.title_filters_categories

@OptIn(ExperimentalResourceApi::class)
val Resource.string.title_filters_favorites: StringResource
    get() = Res.string.title_filters_favorites

@OptIn(ExperimentalResourceApi::class)
val Resource.string.title_filters_formats: StringResource
    get() = Res.string.title_filters_formats

@OptIn(ExperimentalResourceApi::class)
val Resource.string.title_jobs: StringResource
    get() = Res.string.title_jobs

@OptIn(ExperimentalResourceApi::class)
val Resource.string.title_plan: StringResource
    get() = Res.string.title_plan

@OptIn(ExperimentalResourceApi::class)
val Resource.string.title_plan_partner: StringResource
    get() = Res.string.title_plan_partner

@OptIn(ExperimentalResourceApi::class)
val Resource.string.title_notif_reminder_talk: StringResource
    get() = Res.string.title_notif_reminder_talk

@OptIn(ExperimentalResourceApi::class)
val Resource.string.title_ticket: StringResource
    get() = Res.string.title_ticket

@OptIn(ExperimentalResourceApi::class)
val Resource.string.title_schedule_detail: StringResource
    get() = Res.string.title_schedule_detail
