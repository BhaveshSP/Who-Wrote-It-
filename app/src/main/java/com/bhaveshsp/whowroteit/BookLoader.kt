package com.bhaveshsp.whowroteit

import android.content.Context
import androidx.annotation.NonNull
import androidx.loader.content.AsyncTaskLoader

class BookLoader:AsyncTaskLoader<String> {
    var queryString: String?
    constructor(@NonNull context:Context,mQueryString: String?):super(context){
        queryString=mQueryString

    }
    override fun loadInBackground(): String? {
        return NetworkUtils().getBookInfo(queryString)
    }

    override fun onStartLoading() {
        super.onStartLoading()
        forceLoad()
    }
}