/*
 * Copyright (c) 2023.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.app.citrarb.features.connect.data.models.requests

data class CreateOccupationRequest(
    val category: String,
    val description: String,
    val jobTitle: String,
    val name: String,
    val phone: String
)