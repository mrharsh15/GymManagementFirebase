package com.codewithme.gymmanagement.model

data class Member (
    var memberId: String = "",
    var name: String = "",
    var gender: String = "",
    var mobileNum: String = "",
    var joiningDate: String = "",
    var expiryData: String = ""
        ): java.io.Serializable