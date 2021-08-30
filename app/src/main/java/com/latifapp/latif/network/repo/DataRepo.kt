package com.latifapp.latif.network.repo

import com.latifapp.latif.data.models.*
import com.latifapp.latif.network.ResultWrapper
import retrofit2.http.Query

interface DataRepo {
    suspend fun getBlogsList(
        page: Int,
        category: Int?
    ): ResultWrapper<ResponseModel<List<BlogsModel>>>

    suspend fun getSearchBlogs(
        txt: String,
        page: Int
    ): ResultWrapper<ResponseModel<List<BlogsModel>>>

    suspend fun getBlogsCategoryList(): ResultWrapper<ResponseModel<List<CategoryItemsModel>>>
    suspend fun getCategoriesList(type: Int): ResultWrapper<ResponseModel<List<CategoryModel>>>
    suspend fun getCategoriesListFromUrl(url: String): ResultWrapper<ResponseModel<List<CategoryModel>>>
    suspend fun getAdsTypeList(): ResultWrapper<ResponseModel<List<AdsTypeModel>>>
    suspend fun getCreateForm(type: String,isSell:Boolean): ResultWrapper<ResponseModel<SellFormModel>>
    suspend fun createFilterForm(type: String): ResultWrapper<ResponseModel<SellFormModel>>
    suspend fun saveForm(
        url: String,
        model: SaveformModelRequest
    ): ResultWrapper<ResponseModel<SellFormModel>>

    suspend fun saveFilter(
        url: String,
        model: SaveformModelRequest
    ): ResultWrapper<ResponseModel<List<AdsModel>>>

    suspend fun getNearestAds(
        type: String?,
        lat: Double,
        lag: Double,
        category: Int?,
        page: Int
    ): ResultWrapper<ResponseModel<List<AdsModel>>>

    suspend fun getAdDetails(id: Int?): ResultWrapper<ResponseModel<AdsModel>>
    suspend fun createBlog(createBlogsModel: CreateBlogsModel): ResultWrapper<ResponseModel<BlogsModel>>
    suspend fun getDetailsOfBlog(id: Int?): ResultWrapper<ResponseModel<BlogsModel>>
    suspend fun getSubscribeList(page: Int): ResultWrapper<ResponseModel<List<SubscribeModel>>>
    suspend fun getSubscribeDetails(id: String): ResultWrapper<ResponseModel<SubscribeModel>>
    suspend fun getReportedReasonsList(): ResultWrapper<ResponseModel<List<ReportedReasonsList>>>

    suspend fun login(loginRequest: LoginRequest): ResultWrapper<LoginResponse>
    suspend fun getUserInfo(): ResultWrapper<ResponseModel<UserModel>>
    suspend fun register(body: RegisterRequest): ResultWrapper<ResponseModel<UserModel>>
    suspend fun editProfile(body: RegisterRequest): ResultWrapper<ResponseModel<UserModel>>
    suspend fun myAds(page: Int): ResultWrapper<ResponseModel<List<AdsModel>>>
    suspend fun favAds(page: Int): ResultWrapper<ResponseModel<List<FavModel>>>
    suspend fun reportAd(reportedRequestAd: ReportedRequestAd): ResultWrapper<ResponseModel<AdsModel>>
    suspend fun favAd(reportedRequestAd: ReportedRequestAd): ResultWrapper<ResponseModel<AdsModel>>
    suspend fun activateAd(activeAd: Boolean, id: Int?): ResultWrapper<ResponseModel<Any>>
    suspend fun getPolices(): ResultWrapper<ResponseModel<PolicesModel>>
    suspend fun getAllCategories(page: Int): ResultWrapper<ResponseModel<List<CategoryModel>>>
    suspend fun setIntrestCategories(list: MutableList<Int?>,id:String): ResultWrapper<ResponseModel<Any>>
    suspend fun getIntrestCategories(): ResultWrapper<ResponseModel<List<CategoryItemsModel>>>

    suspend fun sendMsg(body: SendMsgBody): ResultWrapper<ResponseModel<String>>
    suspend fun getChatOfRoom(
        page: String?,
        room: String?
    ): ResultWrapper<ResponseModel<List<ChatResponseModel>>>

    suspend fun getAllMyRooms(page: Int): ResultWrapper<ResponseModel<List<MsgNotification>>>
    suspend fun checkIfHaveRoom(ads: Int): ResultWrapper<ResponseModel<String>>
    suspend fun getCountries(): ResultWrapper<ResponseModel<List<CountryModel>>>
    suspend fun getCities(countyId: String): ResultWrapper<ResponseModel<List<CityModel>>>
    suspend fun logout(id: String): ResultWrapper<ResponseModel<Boolean>>
}