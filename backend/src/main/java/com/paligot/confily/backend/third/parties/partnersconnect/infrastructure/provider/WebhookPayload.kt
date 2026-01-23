package com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartnersConnectWebhookPayload(
    val eventType: WebhookEventType,
    val partnership: PartnershipDetail,
    val company: Company,
    val event: EventSummary,
    val jobs: List<JobOffer>,
    val timestamp: String
)

enum class WebhookEventType {
    CREATED,
    UPDATED,
    DELETED
}

@Serializable
data class PartnershipDetail(
    val id: String,
    val phone: String? = null,
    val language: String,
    val emails: List<String> = emptyList(),
    @SerialName("validated_pack")
    val validatedPack: PartnershipPack? = null,
    @SerialName("process_status")
    val processStatus: PartnershipProcessStatus,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
class PartnershipPack(
    val id: String,
    val name: String
)

@Serializable
data class PartnershipProcessStatus(
    @SerialName("validated_at")
    val validatedAt: String? = null,
    @SerialName("declined_at")
    val declinedAt: String? = null,
    @SerialName("billing_status")
    val billingStatus: String? = null
)

@Serializable
enum class InvoiceStatus {
    @SerialName("pending")
    PENDING,

    @SerialName("sent")
    SENT,

    @SerialName("paid")
    PAID
}

@Serializable
data class Company(
    val id: String,
    val name: String,
    @SerialName("head_office")
    val headOffice: Address?,
    val siret: String?,
    val vat: String?,
    val description: String?,
    @SerialName("site_url")
    val siteUrl: String?,
    val medias: Media?,
    val status: CompanyStatus,
    val socials: List<Social>
)

@Serializable
enum class CompanyStatus {
    /**
     * Normal operational company (default for existing records).
     */
    @SerialName("active")
    ACTIVE,

    /**
     * Soft-deleted company (preserved for historical integrity).
     */
    @SerialName("inactive")
    INACTIVE
}

@Serializable
class Media(
    val original: String,
    @SerialName("png_1000")
    val png1000: String,
    @SerialName("png_500")
    val png500: String,
    @SerialName("png_250")
    val png250: String
)

@Serializable
class Address(
    val address: String,
    val city: String,
    @SerialName("zip_code")
    val zipCode: String,
    val country: String
) {
    val fullAddress: String
        get() = "$address, $zipCode $city, $country"
}

@Serializable
class Social(
    val type: String,
    val url: String
)

@Serializable
class EventSummary(
    val slug: String,
    val name: String
)

@Serializable
data class JobOffer(
    val id: String,
    val title: String,
    val url: String
)
