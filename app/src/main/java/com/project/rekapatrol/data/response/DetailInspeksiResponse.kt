package com.project.rekapatrol.data.response

data class DetailInspeksiResponse(
	val data: Data? = null
)

data class DataDetailInspeksi(
	val checkupDate: String? = null,
	val picId: Any? = null,
	val suitability: Int? = null,
	val criteria: CriteriaDetailInspeksi? = null,
	val findings: List<FindingsItemDetailInspeksi?>? = null,
	val criteriaId: Int? = null,
	val createdAt: String? = null,
	val actionPath: Any? = null,
	val workerId: Int? = null,
	val inspectionLocation: String? = null,
	val actionDescription: Any? = null,
	val updatedAt: String? = null,
	val id: Int? = null,
	val findingsDescription: String? = null,
	val value: String? = null
)

data class CriteriaDetailInspeksi(
	val updatedAt: String? = null,
	val createdAt: String? = null,
	val id: Int? = null,
	val criteriaType: String? = null,
	val locationId: Int? = null,
	val criteriaName: String? = null
)

data class FindingsItemDetailInspeksi(
	val findableId: Int? = null,
	val updatedAt: String? = null,
	val findableType: String? = null,
	val imagePath: String? = null,
	val createdAt: String? = null,
	val id: Int? = null
)

