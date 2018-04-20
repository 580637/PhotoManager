package com.example.photos.photomanager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view , "Replace with your own action" , Snackbar.LENGTH_LONG)
                    .setAction("Action" , null).show()
        }


        // Example of a call to a native method
        sample_text.text = stringFromJNI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main , menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            R.id.action_camera -> {
                Toast.makeText(this , "您点击了拍照" , Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_call -> {
                ActivityCompat.requestPermissions(this , Array(1 , { android.Manifest.permission.CALL_PHONE }) , 100)
//                ActivityCompat.requestPermissions(this , new String []{android.Manifest.permission.CALL_PHONE} , REQUESTCODE);
                val intent =  Intent()
                intent.setAction(Intent.ACTION_CALL)
                intent.setData(Uri.parse("tel:13823159421"))
                startActivity(intent)
//                Toast.makeText(this, "您点击了打电话", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
