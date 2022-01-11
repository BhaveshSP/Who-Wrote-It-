package com.bhaveshsp.whowroteit

import android.net.Uri
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


const val BOOK_BASE_URL="https://www.googleapis.com/books/v1/volumes?"
const val QUERY_PARAM="q"
const val MAX_RESULT="maxResults"
const val PRINT_TYPE="printType"

class NetworkUtils {

    lateinit var urlConnection: HttpURLConnection
    lateinit var reader: BufferedReader
    lateinit var bookJSONString:String
     fun getBookInfo(queryString: String?): String {

        try {

            val buildURI= Uri.parse(BOOK_BASE_URL).buildUpon()
            buildURI.appendQueryParameter(QUERY_PARAM, queryString)
            buildURI.appendQueryParameter(MAX_RESULT,"10")
            buildURI.appendQueryParameter(PRINT_TYPE,"books")
            buildURI.build()


            val requestURL= URL(buildURI.toString())


            urlConnection=requestURL.openConnection() as HttpURLConnection
            urlConnection.requestMethod="GET"
            urlConnection.connect()

            val inputStream=urlConnection.inputStream
            reader=BufferedReader(InputStreamReader(inputStream))
            val builder=StringBuilder()
            var line=reader.readLine()
            while (line!=null){
                builder.append(line)
                builder.append("\n")
                line=reader.readLine()
            }
            if (builder.isEmpty()){
                return ""
            }
            bookJSONString=builder.toString()



        }catch (e:IOException){
            e.printStackTrace()
        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect()
            }
            if (reader!=null){
                try {
                    reader.close()
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }

        }
        Log.d("Bhavesh",bookJSONString)
        return bookJSONString

    }

}