package com.latifapp.latif.data.models


data class ReportedRequestAd(val adId:Int,val reason:String?,val otherReason:String?=null,val type:String,)
data class ReportedReasonsList(val id:Int,val value:String?,val valueAr:String?,)
