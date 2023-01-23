/*
 * Copyright (c) 2023.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.app.citrarb.features.members.data.models.responses

data class Member(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val photo: String,
    val role: String,
    val updatedAt: String,
    val username: String
)