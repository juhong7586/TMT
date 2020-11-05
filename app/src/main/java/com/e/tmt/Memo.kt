package com.e.tmt

import com.google.gson.annotations.SerializedName


data class Memo(var id: Int, var editor: String, var title: String, var content: String, var datetime: String)

data class Result(
    var message: Message
)

data class Message(
    var result: Memo
)

