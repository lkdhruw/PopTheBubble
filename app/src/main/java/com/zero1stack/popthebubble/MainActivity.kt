package com.zero1stack.popthebubble

import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.media.SoundPool.OnLoadCompleteListener
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.math.floor

class MainActivity : AppCompatActivity(), OnLoadCompleteListener {

    var maxWidth: Float = 0.0f
    var maxHeight: Float = 0.0f
    var noOfCol: Int = 0
    var noOfRow: Int = 0

    private val soundPoolBuilder: SoundPool.Builder = SoundPool.Builder()
    val soundPool: SoundPool = soundPoolBuilder.build()
    var soundId1: Int = 0
    var soundId2: Int = 0
    var stream1: Boolean = false
    var stream2: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        soundPoolBuilder.setMaxStreams(2)
        soundId1 = soundPool.load(this,R.raw.bubble_pop_2,1)
        soundId2 = soundPool.load(this,R.raw.bubble_pop_2,1)

        val displayMetrics: DisplayMetrics = baseContext.resources.displayMetrics

        maxWidth = displayMetrics.widthPixels/displayMetrics.density
        maxHeight = displayMetrics.heightPixels/displayMetrics.density

        noOfCol = floor((maxWidth/64).toDouble()).toInt()
        noOfRow = floor((maxHeight/64).toDouble()).toInt()
        noOfRow--
        /*
        Log.i("LOG","density "+displaymetrics.density.toString())
        Log.i("LOG","densityDpi "+displaymetrics.densityDpi.toString())
        Log.i("LOG","scaledDensity "+displaymetrics.scaledDensity.toString())
        Log.i("LOG","width px "+displaymetrics.widthPixels.toString())
        Log.i("LOG","height px "+displaymetrics.heightPixels.toString())
        Log.i("LOG","xdpi "+displaymetrics.xdpi.toString())
        Log.i("LOG","ydpi "+displaymetrics.ydpi.toString())
        Log.i("LOG","table height "+ bubbleGrid.height.toString())
        */
        //winWidth.setText((displaymetrics.densityDpi).toString())
        //winHeight.setText((displaymetrics.widthPixels).toString())


        generateGrid()

    }

    override fun onLoadComplete(soundPool: SoundPool?, soundId: Int, status: Int) {
        //soundPool?.play(soundId,1.0f,1.0f,1,0,1.0f)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                bubbleGrid.removeAllViews()
                generateGrid()
                true
            }
            R.id.action_home -> {
                startActivity(Intent(this,HomeActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun playSound() {
        val t = Thread(Runnable {
            val popSound: MediaPlayer = MediaPlayer.create(this, R.raw.bubble_pop_2)
            popSound.start()
        }).start()
    }
    private fun generateGrid() {
        val popSound: MediaPlayer = MediaPlayer.create(this, R.raw.bubble_pop_2)
        var matrix: ColorMatrix = ColorMatrix()
        matrix.setSaturation(0.0f)
        val filter: ColorMatrixColorFilter = ColorMatrixColorFilter(matrix)
        for (row in 1..noOfRow) {
            val tableRowParam = TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            val tableRow: TableRow = TableRow(this)
            tableRow.layoutParams = tableRowParam

            val linearLayoutParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
            val linearLayout: LinearLayout = LinearLayout(this)
            //linearLayout.layoutParams = linearLayoutParam
            linearLayout.orientation = LinearLayout.HORIZONTAL

            val imgDim: Int = resources.getDimension(R.dimen.bubble_height.toInt()).toInt()
            val param = LinearLayout.LayoutParams(imgDim,imgDim)
            val margin: Int = resources.getDimension(R.dimen.bubble_margin_small.toInt()).toInt()
            param.setMargins(margin,margin,margin,margin)

            for (col in 1..noOfCol) {

                val imageView: ImageView = ImageView(this)

                imageView.setImageResource(R.mipmap.ic_launcher_foreground)
                imageView.layoutParams = param

                imageView.setOnClickListener {
                    Log.d("Click",row.toString() +" "+ col.toString())
                    imageView.setColorFilter(filter)

                    imageView.isEnabled = false
                    /*
                    val t = Thread(Runnable {
                        val soundPoolBuilder: SoundPool.Builder = SoundPool.Builder()
                        soundPoolBuilder.setMaxStreams(1)
                        val soundPool: SoundPool = soundPoolBuilder.build()
                        val soundId: Int = soundPool.load(this,R.raw.bubble_pop_2,1)
                        soundPool.setOnLoadCompleteListener(this)
                        /*
                        fun onLoadComplete(soundPool: SoundPool, soundId: Int, status: Int) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

                        }*/
                        soundPool.play(soundId,1.0f,1.0f,1,0,1.0f)
                    }).start()
                    */

                    if (!stream1) {
                        soundPool.play(soundId1,1.0f,1.0f,0,0,1.0f)
                        stream1 = true
                        stream2 = false
                    } else {
                        soundPool.play(soundId2,1.0f,1.0f,0,0,1.0f)
                        stream1 = false
                        stream2 = true
                    }



                    /*
                    if (popSound.isPlaying) {
                        playSound()
                    } else {
                        popSound.start()
                    }
                    */
                }
                linearLayout.addView(imageView)
            }
            tableRow.addView(linearLayout)
            bubbleGrid.addView(tableRow)
        }

    }

}
