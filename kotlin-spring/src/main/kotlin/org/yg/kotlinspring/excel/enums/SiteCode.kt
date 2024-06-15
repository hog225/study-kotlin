package org.yg.kotlinspring.excel.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class SiteCode {

    S,
    L;

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromValue(v: String) = values().firstOrNull { it.name == v.uppercase() }
    }
}
