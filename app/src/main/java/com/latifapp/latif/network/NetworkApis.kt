package com.example.postsapplication.network

import androidx.annotation.Nullable
import com.latifapp.latif.data.models.*
import retrofit2.Call
import retrofit2.http.*


interface NetworkApis {
    @GET("api/public/blogs")
    suspend fun getBlogs(
        @Query("page") page: Int,
        @Nullable @Query("category") category: Int?
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

    @GET("api/public/cat-by-adType/type={type}")
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


    @POST("api/public/account/registration")
    suspend fun register(
        @Body model: RegisterRequest
    ): ResponseModel<RegisterRequest>


    @POST("login")
    fun login(@Body body: LoginRequest): Call<Void>

    @POST("api/public/reportedAds/makeReport")
    suspend fun reportAd(@Body reportedRequestAd: ReportedRequestAd): ResponseModel<AdsModel>
    @POST("api/public/reportedAds/addToInterestList")
    suspend fun favAd(@Body reportedRequestAd: ReportedRequestAd): ResponseModel<AdsModel>
}