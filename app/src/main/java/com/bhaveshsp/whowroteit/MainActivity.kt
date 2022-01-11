package com.bhaveshsp.whowroteit

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity(),LoaderManager.LoaderCallbacks<String> {
    private lateinit var titleText:TextView
    private lateinit var authorText:TextView
    private lateinit var bookName:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         titleText=findViewById(R.id.bookTitle)
         authorText=findViewById(R.id.bookAuthor)
         bookName=findViewById(R.id.searchTitle)
        if (supportLoaderManager.getLoader<String>(0)!=null){
            supportLoaderManager.initLoader(0,null,this)
        }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun searchBook(view: View){
        val queryString=bookName.text.toString()
        val inputManager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
        val connectivityManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetwork
        if ((networkInfo!=null) and (queryString.isNotEmpty())) {
            val bundle=Bundle()
            bundle.putString("queryString",queryString)
            supportLoaderManager.restartLoader(0,bundle,this)
            titleText.text = getString(R.string.loading)
            authorText.text = ""
        }
        else{
            if (queryString.isEmpty()){
                Toast.makeText(this,"Enter Something!!!",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Internet is Not Available",Toast.LENGTH_SHORT).show()
            }
        }
    }


    //Loader Methods
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> {
        val queryString=args?.getString("queryString")
        return BookLoader(this,queryString)
    }

    override fun onLoadFinished(loader: Loader<String>, data: String?) {
        Toast.makeText(this,"Loader Stared",Toast.LENGTH_SHORT).show()
        if (data!=null) {
            try {
                val jsonObject = JSONObject(data)
                val itemsArray = jsonObject.getJSONArray("items")
                var i = 0
                var title = ""
                var author = ""
                while ((i < itemsArray.length()) and (title.isEmpty() and author.isEmpty())) {
                    val book = itemsArray.getJSONObject(i)
                    val volumeInfo = book.getJSONObject("volumeInfo")
                    try {
                        title = volumeInfo.getString("title")
                        author = volumeInfo.getString("authors")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    i++
                    if (title.isNotEmpty() and author.isNotEmpty()) {
                        bookTitle.text = title
                        bookAuthor.text = author

                    } else {
                        bookTitle.text = getString(R.string.no_results)
                        bookAuthor.text = ""
                    }

                }
            } catch (e: JSONException) {
                bookTitle.text = getString(R.string.no_results)
                bookAuthor.text = ""
                e.printStackTrace()
            }
        }
    }

    override fun onLoaderReset(loader: Loader<String>) {
        TODO("Not yet implemented")
    }
}