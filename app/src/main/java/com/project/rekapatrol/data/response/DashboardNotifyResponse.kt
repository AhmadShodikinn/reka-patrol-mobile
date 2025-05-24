package com.project.rekapatrol.data.response

import com.google.gson.annotations.SerializedName

data class DashboardNotifyResponse(

	@field:SerializedName("data")
	val data: DataNotification? = null
)

data class SafetyPatrols(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("solved")
	val solved: Int? = null,

	@field:SerializedName("unsolved")
	val unsolved: Int? = null
)

data class DataNotification(

	@field:SerializedName("inspections")
	val inspections: Inspections? = null,

	@field:SerializedName("safety_patrols")
	val safetyPatrols: SafetyPatrols? = null
)

data class Inspections(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("solved")
	val solved: Int? = null,

	@field:SerializedName("unsolved")
	val unsolved: Int? = null
)
