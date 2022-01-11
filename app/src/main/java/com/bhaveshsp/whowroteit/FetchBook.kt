package com.bhaveshsp.whowroteit

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class FetchBook(textViewTitle:TextView,textViewAuthor:TextView,mcontext:Context):AsyncTask<String,Void,String> (){
    val bookTitle=WeakReference(textViewTitle)
    val bookAuthor=WeakReference(textViewAuthor)
    private val context=mcontext



    override fun doInBackground(vararg params: String?): String {

        val networkUtils=NetworkUtils()
        return networkUtils.getBookInfo(params[0])
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        try {
            val jsonObject=JSONObject(result)
            val itemsArray=jsonObject.getJSONArray("items")
            var i=0
            var title=""
            var author=""
            while ((i<itemsArray.length()) and (title.isEmpty() and author.isEmpty())){
                val book=itemsArray.getJSONObject(i)
                val volumeInfo=book.getJSONObject("volumeInfo")
                try {
                    title=volumeInfo.getString("title")
                    author=volumeInfo.getString("authors")
                }catch (e:Exception){
                    e.printStackTrace()
                }
                i++
                if (title.isNotEmpty() and author.isNotEmpty()){
                    bookTitle.get()?.text=title
                    bookAuthor.get()?.text=author

                }else{
                    bookTitle.get()?.text=context.getString(R.string.no_results)
                    bookAuthor.get()?.text=""
                }

            }
        }catch (e:JSONException){
            bookTitle.get()?.text=context.getString(R.string.no_results)
            bookAuthor.get()?.text=""
            e.printStackTrace()
        }
    }
}