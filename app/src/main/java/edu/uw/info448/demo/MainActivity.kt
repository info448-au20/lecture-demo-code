package edu.uw.info448.demo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat


class MainActivity : AppCompatActivity() {

    private val TAG = "**MAIN**"

    private val REQUEST_IMAGE_CAPTURE = 1

    companion object {
        const val EXAMPLE_MESSAGE_KEY = "edu.uw.info448.demo.message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val launchButton = findViewById<View>(R.id.btn_launch)
        launchButton.setOnClickListener {
            Log.v(TAG, "Launch button pressed")

            //Explicit Intent
            //                                    context            target
            val myIntent = Intent(this@MainActivity, SecondActivity::class.java)
            myIntent.putExtra(EXAMPLE_MESSAGE_KEY, "Hello from MainActivity! It's lovely here")
            startActivity(myIntent) //sends the Intent

        }

    }

    fun callNumber(v: View) {
        Log.v(TAG, "Call button pressed")

        //Implicit Intent
        //Action - what we want to do - dial a phone
        //Data - what we want to do that with - what phone number
        val intent = Intent(Intent.ACTION_DIAL) //action
        intent.data = Uri.parse("tel:205-685-1622") //data

        if(intent.resolveActivity(packageManager) != null) {
            startActivity(intent) //send our IMPLICIT intent
        } else {
            Log.d(TAG, "No supporting activity found")
        }

    }

    fun takePicture(v: View?) {
        Log.v(TAG, "Camera button pressed")

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_CAPTURE) {
            if(resultCode == Activity.RESULT_OK) {
               val extras = data?.extras?.apply {
                   val imageBitmap = this.get("data") as Bitmap //get the thumbnail
                   findViewById<ImageView>(R.id.img_thumbnail).setImageBitmap(imageBitmap)
               }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val shareItem = menu.findItem(R.id.menu_item_share)
        val myShareActionProvider = MenuItemCompat.getActionProvider(shareItem) as ShareActionProvider

        val intent = Intent(Intent.ACTION_DIAL) //action
        intent.data = Uri.parse("tel:205-685-1622") //data

        myShareActionProvider.setShareIntent(intent)

        return true //we did make a menu, be sure and show
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_hello -> {
                //do our stuff
                Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_item_call -> {
                Log.v(TAG, "Calling...")
                //callNumber()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}
