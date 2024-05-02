package com.algostack.smartcircuithouse.utils

import android.content.Context

class TokenManager (context : Context) {
    private var prefs = context.getSharedPreferences(Constants.PREFS_TOKEN_FILE, Context.MODE_PRIVATE)


    fun saveUid(uid: String){
        val editor = prefs.edit()
        editor.putString(Constants.USER_UID, uid)
        editor.apply()
    }


    fun getUid(): String? {
        return prefs.getString(Constants.USER_UID, null)
    }

    fun updateUid(uid: String){
        val editor = prefs.edit()
        editor.putString(Constants.USER_UID, uid)
        editor.apply()
    }

    fun clearUid(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }


}