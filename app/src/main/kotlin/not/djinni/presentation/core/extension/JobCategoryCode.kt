package not.djinni.presentation.core.extension

import not.djinni.R
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.presentation.core.components.base.model.TextData

fun JobCategoryCode.toDisplayName(): TextData = when (this) {
    JobCategoryCode.SOFTWARE_DEV -> R.string.job_category_software_dev.toTextData()
    JobCategoryCode.DATA_SCIENCE -> R.string.job_category_data_science.toTextData()
    JobCategoryCode.DEVOPS -> R.string.job_category_devops.toTextData()
    JobCategoryCode.QA -> R.string.job_category_qa.toTextData()
    JobCategoryCode.PRODUCT_MGMT -> R.string.job_category_product_mgmt.toTextData()
    JobCategoryCode.DESIGN -> R.string.job_category_design.toTextData()
    JobCategoryCode.MARKETING -> R.string.job_category_marketing.toTextData()
    JobCategoryCode.SALES -> R.string.job_category_sales.toTextData()
    JobCategoryCode.HR -> R.string.job_category_hr.toTextData()
    JobCategoryCode.FINANCE -> R.string.job_category_finance.toTextData()
    JobCategoryCode.OPERATIONS -> R.string.job_category_operations.toTextData()
    JobCategoryCode.SUPPORT -> R.string.job_category_support.toTextData()
}
