package com.project.rekapatrol.data.response

data class UpdateInspeksiResponse(
	val data: DataUpdateInspeksi? = null
)

data class DataUpdateInspeksi(
	val checkupDate: String? = null,
	val picId: String? = null,
	val suitability: Int? = null,
	val criteria: CriteriaUpdateInspeksi? = null,
	val findings: List<FindingsItemUpdateInspeksi?>? = null,
	val criteriaId: Int? = null,
	val createdAt: String? = null,
	val actionPath: String? = null,
	val workerId: Int? = null,
	val inspectionLocation: String? = null,
	val actionDescription: String? = null,
	val updatedAt: String? = null,
	val id: Int? = null,
	val findingsDescription: String? = null,
	val value: String? = null
)

data class FindingsItemUpdateInspeksi(
	val findableId: Int? = null,
	val updatedAt: String? = null,
	val findableType: String? = null,
	val imagePath: String? = null,
	val createdAt: String? = null,
	val id: Int? = null
)

data class CriteriaUpdateInspeksi(
	val updatedAt: String? = null,
	val createdAt: String? = null,
	val id: Int? = null,
	val criteriaType: String? = null,
	val locationId: Int? = null,
	val criteriaName: String? = null
)

