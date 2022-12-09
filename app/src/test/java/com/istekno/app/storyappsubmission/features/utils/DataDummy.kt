package com.istekno.app.storyappsubmission.features.utils

import com.istekno.app.storyappsubmission.db.model.Login
import com.istekno.app.storyappsubmission.db.model.Register
import com.istekno.app.storyappsubmission.db.model.Story
import com.istekno.app.storyappsubmission.db.model.Validator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {
    fun generateDummyRegisterRequest() =
        Register.Request(
            "Satoru 97",
            "satoru@gmail.com",
            "myPassword"
        )

    fun generateDummyRegisterResponse() =
        Validator.Response(
            false,
            "succes register account"
        )

    fun generateDummyRegisterResponseError() =
        Validator.Response(
            true,
            "failed register account"
        )

    fun generateDummyLoginRequest() =
        Login.Request(
            "myEmail",
            "myPassword"
        )

    fun generateDummyLoginResponse() =
        Login.Response(
            false,
            "succes login",
            Login.Response.Data(
                "story-J2C71WNnXHgxSzoH",
                "Nanami 88",
                dummyToken()
            )
        )

    fun generateDummyLoginResponseError() =
        Login.Response(
            true,
            "failed login",
            null
        )

    fun generateDummyStories(): List<Story.Response.Data> {
        val stories = arrayListOf<Story.Response.Data>()

        (0..9).forEach {
            val story = Story.Response.Data(
                "story-05JhSrOsY6qeIhMy",
                "Vegapunk $it",
                "Dummy Story $it",
                "https://story-api.dicoding.dev/images/stories/photos-1668324251486_hTQEw5HW.jpg",
                "2022-11-13T06:52:08.984Z",
                -6.900901,
                107.607925
            )
            stories.add(story)
        }

        return stories
    }

    fun generateDummyStoryResponse(): Story.Response {
        val stories = arrayListOf<Story.Response.Data>()

        (0..9).forEach {
            val story = Story.Response.Data(
                "story-05JhSrOsY6qeIhMy",
                "Germa $it",
                "Dummy Story $it",
                "https://story-api.dicoding.dev/images/stories/photos-1668324251486_hTQEw5HW.jpg",
                "2022-11-13T06:52:08.984Z",
                -6.900901,
                107.607925
            )
            stories.add(story)
        }

        return Story.Response(
            false,
            "Stories fetched successfully",
            stories
        )
    }

    fun generateDummyStoryResponseEmpty() =
        Story.Response(
            false,
            "Stories fetched successfully",
            arrayListOf()
        )

    fun generateDummyStoryResponseError() =
        Story.Response(
            true,
            "Stories fetched error",
            arrayListOf()
        )

    fun generateDummyAddStoryResponse() =
        Validator.Response(
            false,
            "succes upload story"
        )

    fun dummyMultipartFile() =
        MultipartBody.Part.create("MyParams".toRequestBody())

    fun dummyReqBody() = "MyReqBody".toRequestBody("text/plain".toMediaType())

    fun dummyToken() =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXNHamQzZWx0Wk1zckl1M3IiLCJpYXQiOjE2NTcyMTc2NjV9.ZlZaTNeZX3Db4KYwTkIaiUTBy5oX-3DkSmlSnpSuZws"
}