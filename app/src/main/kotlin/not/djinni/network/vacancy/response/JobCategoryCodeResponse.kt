package not.djinni.network.vacancy.response

import kotlinx.serialization.Serializable

@Serializable
enum class JobCategoryCodeResponse {
    SOFTWARE_DEV,
    DATA_SCIENCE,
    DEVOPS,
    QA,
    PRODUCT_MGMT,
    DESIGN,
    MARKETING,
    SALES,
    HR,
    FINANCE,
    OPERATIONS,
    SUPPORT
}