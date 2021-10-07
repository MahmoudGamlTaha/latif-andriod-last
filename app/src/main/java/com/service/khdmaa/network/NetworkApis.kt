package com.service.khdmaa.network

import androidx.annotation.Nullable
import com.service.khdmaa.data.models.*
import okhttp3.MultipartBody
import retrofit2.http.*


interface NetworkApis {
    @GET("api/public/blogs")
    suspend fun getBlogs(
        @Query("page") page: Int
    ): ResponseModel<List<BlogsModel>>
    @GET("api/public/blogs/blog-by-category")
    suspend fun getBlogsWithCategory(
        @Query("id") id: Int?,
        @Query("page") page: Int

    ): ResponseModel<List<BlogsModel>>

    @GET("api/public/blogs/id={id}")
    suspend fun getBlogDetails(@Path("id") id: Int?): ResponseModel<BlogsModel>

    @GET("api/public/blogs/keyword={keyword}")
    suspend fun getSearchBlogs(
        @Path("keyword") txt: String,
        @Query("page") page: Int
    ): ResponseModel<List<BlogsModel>>

    @GET("api/public/blogCategory")
    suspend fun getBlogsCategoryList(): ResponseModel<List<CategoryItemsModel>>


    @GET("api/public/ads-type/list")
    suspend fun getAdsTypeList(): ResponseModel<List<AdsTypeModel>>

    @GET("api/public/cat-by-adType/type={type}/0/andriod")
    suspend fun getCatsTypeList(@Path("type") type: Int): ResponseModel<List<CategoryModel>>

    @GET
    suspend fun getCatsTypeListUrl(@Url url: String): ResponseModel<List<CategoryModel>>

    @GET("api/public/ads/get-create-form")
    suspend fun getCreateForm(@Query("adType") type: String): ResponseModel<SellFormModel>

    @GET("api/public/ads/get-filter-form")
    suspend fun createFilterForm(@Query("adType") type: String): ResponseModel<SellFormModel>

    @GET("api/public/ads/nearest")
    suspend fun getNearestAds(
        @Nullable @Query("type") type: String?,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Nullable @Query("category") category: Int?,
        @Query("page") page: Int
    ): ResponseModel<List<AdsModel>>

    @GET("api/public/ads/myAds")
    suspend fun getMyAds(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 20
    ): ResponseModel<List<AdsModel>>

    @GET("api/public/reportedAds/interested")
    suspend fun getFavAds(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 20
    ): ResponseModel<List<FavModel>>

    @GET("api/public/ads/ad-by-Id")
    suspend fun getAdDetails(@Nullable @Query("id") id: Int?): ResponseModel<AdsModel>

    @GET("api/public/reasons")
    suspend fun getReportedReasonsList(): ResponseModel<List<ReportedReasonsList>>

    @POST
    suspend fun saveForm(
        @Url url: String,
        @Body model: SaveformModelRequest
    ): ResponseModel<SellFormModel>

    @POST
    suspend fun saveFilter(
        @Url url: String,
        @Body model: SaveformModelRequest
    ): ResponseModel<List<AdsModel>>

    @POST("api/public/blogs/create")
    suspend fun createBlog(@Body body: CreateBlogsModel): ResponseModel<BlogsModel>

    @GET("api/public/subscriptionTypes")
    suspend fun getSubscribeList(@Query("page") page: Int): ResponseModel<List<SubscribeModel>>

    @GET("api/public/subscriptionTypes/getById")
    suspend fun getSubscribeDetails(@Query("id") id: String): ResponseModel<SubscribeModel>


    @POST("api/public/account/registration")
    suspend fun register(
        @Body model: RegisterRequest
    ): ResponseModel<UserModel>

    @PUT("api/account/update")
    suspend fun editProfile(
        @Body model: RegisterRequest
    ): ResponseModel<UserModel>

    @POST("api/public/ads/adActivation")
    suspend fun activeAd(
        @Nullable @Query("id") id: Int?,
        @Query("activate") activate: Boolean
    ): ResponseModel<Any>


    @POST("login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("api/public/account/profile")
    suspend fun getUserInfo(): ResponseModel<UserModel>

    @POST("api/public/reportedAds/makeReport")
    suspend fun reportAd(@Body reportedRequestAd: ReportedRequestAd): ResponseModel<AdsModel>

    @POST("api/public/reportedAds/addToInterestList")
    suspend fun favAd(@Body reportedRequestAd: ReportedRequestAd): ResponseModel<AdsModel>

    @GET("api/public/policies")
    suspend fun getPolices(): ResponseModel<PolicesModel>

    @POST("api/public/interest-categories/create")
    suspend fun setIntrestCategories(@Body list: MutableList<Int?>,@Query("userId")id:String): ResponseModel<Any>

    @GET("api/public/my-interest-categories")
    suspend fun getIntrestCategories(): ResponseModel<List<CategoryItemsModel>>

    @GET("api/public/category/page={page}")
    suspend fun getAllCategories(@Path("page") page: Int ): ResponseModel<List<CategoryModel>>

    @POST("api/public/chat/snd-msg")
    suspend fun sendMsg(@Body body: SendMsgBody): ResponseModel<String>

    @GET("api/public/chat/next-page-by-id")
    suspend fun getChatOfRoom(
        @Query("message_id") page: String?,
        @Query("room") room: String?
    ): ResponseModel<List<ChatResponseModel>>

    @GET("api/public/chat/my-chat")
    suspend fun getAllMyRooms(@Query("page") page: Int = 0): ResponseModel<List<MsgNotification>>

    @GET("api/public/chat/check-chat-ads")
    suspend fun checkIfHaveRoom(@Query("ads") ads: Int = 0): ResponseModel<String>

    @GET("api/public/countries")
    suspend fun getCountries(): ResponseModel<List<CountryModel>>

    @GET("api/public/city/find-by-country-id")
    suspend fun getCities(@Query("country") id: String): ResponseModel<List<CityModel>>

    @POST("api/account/logout")
    suspend fun logout(@Query("user") id: String): ResponseModel<Boolean>
    @GET("/api/public/category-interest")
    suspend fun getInterstList():ResponseModel<List<CategoryModel>>

    @Multipart
    @POST("api/public/uploader/upload")
    suspend fun UploadFiles(@Part file: MultipartBody.Part,@Query("module") module: String): ResponseModel<Any>
}