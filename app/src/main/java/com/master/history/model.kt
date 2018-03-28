package com.master.history

import java.io.Serializable

/**
 * Created by 不听话的好孩子 on 2018/3/28.
 */
data class Base<T> constructor(var message: String="", var data: T, var code: Int) : Serializable
data class Data(var date:String="",var ip:String="")
data class Data2(var date:String="",var url:String="")