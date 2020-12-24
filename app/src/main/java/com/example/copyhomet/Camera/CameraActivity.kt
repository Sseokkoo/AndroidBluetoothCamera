package com.example.copyhomet.Camera

import android.Manifest
import android.app.Activity
import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.graphics.Point
import android.graphics.SurfaceTexture
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.copyhomet.Bluetooth.Bluetooth1.BLE.BleProfileService
import com.example.copyhomet.Bluetooth.Bluetooth2.BLE.BleProfileService_2
import com.example.copyhomet.Bluetooth.Bluetooth3.BLE.BleProfileService_3
import com.example.copyhomet.Bluetooth.Bluetooth4.BLE.BleProfileService_4
import com.example.copyhomet.Bluetooth.Bluetooth5.BLE.BleProfileService_5
import com.example.copyhomet.DialogSpeach
import com.example.copyhomet.Main.MainActivity
import com.example.copyhomet.R
import com.jinasoft.hometuser.Camera.Camera2BasicFragment_Back
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import java.io.IOException
import java.util.*

/**
 * Main `Activity` class for the Camera app.
 */


open class CameraActivity : Activity() , SensorEventListener, TextureView.SurfaceTextureListener,MediaPlayer.OnVideoSizeChangedListener{


  /**카메라(pose estimation) 은 코틀린 으로 작성됨   pose estimation 라이브러리 참고**/

  private val lock = Any()
  private var runClassifier = false
  private var drawView: DrawView? = null
  private var classifier: ImageClassifier? = null
  private lateinit var textureView: TextureView
  private lateinit var mediaPlayer: MediaPlayer
  private lateinit var fileDescriptor: AssetFileDescriptor

  private lateinit var SttIntent : Intent
  private var mRecognizer : SpeechRecognizer? = null
  private var tts: TextToSpeech? = null
  private lateinit var btnSTTStart : Button


  var btnPlus : Button? = null
  var btnMinus : Button? = null
  var btnPlay : Button? = null

  var PageOpen : Int = 1



  private val mLoaderCallback = object : BaseLoaderCallback(this) {
    override fun onManagerConnected(status: Int) {    //override = @Override
      when (status) {
        LoaderCallbackInterface.SUCCESS -> isOpenCVInit = true
        LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION -> {
        }
        LoaderCallbackInterface.INIT_FAILED -> {
        }
        LoaderCallbackInterface.INSTALL_CANCELED -> {
        }
        LoaderCallbackInterface.MARKET_ERROR -> {
        }
        else -> {
          super.onManagerConnected(status)
        }
      }
    }
  }


  var sm: SensorManager? = null
  var s : Sensor? = null

  var btnVideo1 : Button? = null
  var btnVideo2 : Button? = null

  var text : TextView? =null

  @RequiresApi(Build.VERSION_CODES.N)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_camera)
    text = findViewById(R.id.tvNoPoint)


    val close : LinearLayout = findViewById(R.id.close);
    close.setOnClickListener{ n->

      onBackPressed()

    }

    btnVideo1 = findViewById(R.id.video1)
    btnVideo2  = findViewById(R.id.video2)

    val drawView : DrawView = findViewById(R.id.videoDrawView)
    classifier = ImageClassifierFloatInception.create(this)
    drawView.setImgSize(classifier!!.imageSizeX, classifier!!.imageSizeY)




    sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager?
    s  = sm?.getDefaultSensor(Sensor.TYPE_ORIENTATION)

    btnPlus =findViewById(R.id.btnPlus)
    btnPlus!!.setOnClickListener { n->
      if(MainActivity.getMain2()!=null) {

        MainActivity.getMain2().PlusAllClick()
      }
    }
    btnMinus =findViewById(R.id.btnMinus)
    btnMinus!!.setOnClickListener { n->
      if(MainActivity.getMain2()!=null) {
        MainActivity.getMain2().MinusAllClick()
      }
    }
    btnPlay = findViewById(R.id.btnPlay)
    btnPlay!!.setOnClickListener { n->
      if(MainActivity.getMain2()!=null) {


        if (BleProfileService.getBleProfile() != null) {
          if (BleProfileService.getBleProfile().connectionState == 2) {
            MainActivity.getMain2().PlayClick()
          }
        } else if (BleProfileService_2.getBleProfile() != null) {
          if (BleProfileService_2.getBleProfile().connectionState == 2) {
            MainActivity.getMain2().PlayClick()
          }
        } else if (BleProfileService_3.getBleProfile() != null) {
          if (BleProfileService_3.getBleProfile().connectionState == 2) {
            MainActivity.getMain2().PlayClick()
          }
        } else if (BleProfileService_4.getBleProfile() != null) {
          if (BleProfileService_4.getBleProfile().connectionState == 2) {
            MainActivity.getMain2().PlayClick()
          }
        } else if (BleProfileService_5.getBleProfile() != null) {
          if (BleProfileService_5.getBleProfile().connectionState == 2) {
            MainActivity.getMain2().PlayClick()
          }
        }else{
          Toast.makeText(this, "연결된 기기가 없습니다.", Toast.LENGTH_SHORT).show()
        }
      }
    }
    val btnPose : Button  = findViewById(R.id.btnPose)
    var poseState :Int = 1

    btnPose.setOnClickListener { n->
      if(poseState ==1) {
        val videodrawView: DrawView = findViewById(R.id.videoDrawView)
        val drawView: DrawView = findViewById(R.id.drawview)
        drawView.visibility = GONE
        videodrawView.visibility = GONE
        poseState =0
      }else{
        val videodrawView: DrawView = findViewById(R.id.videoDrawView)
        val drawView: DrawView = findViewById(R.id.drawview)
        drawView.visibility = VISIBLE
        videodrawView.visibility = VISIBLE
        poseState =1
      }

    }
    val btnChange : LinearLayout  = findViewById(R.id.btnChange)
    btnChange.setOnClickListener { n->
      val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
      val camera2BasicFragment = Camera2BasicFragment()
      val camera2BasicFragment_Back = Camera2BasicFragment_Back()
//      fragmentTransaction.replace(R.id.container,camera2BasicFragment).commit()

      val prefs: SharedPreferences = applicationContext.getSharedPreferences("info", Context.MODE_PRIVATE)
      if(prefs.getString("cameraIndex", "front").equals("front")) {
        fragmentTransaction.replace(R.id.container, camera2BasicFragment).commit()
        val editor = prefs!!.edit()
        editor.putString("cameraIndex", "back")
        editor.apply()
      }else{
        fragmentTransaction.replace(R.id.container, camera2BasicFragment_Back).commit()
//
        val editor = prefs!!.edit()
        editor.putString("cameraIndex", "front")
        editor.apply()
      }
    }



//    val btnPlay : Button = findViewById(R.id.btnPlay)
//    btnPlay.setOnClickListener { n->
//      Toast.makeText(this, "하잇", Toast.LENGTH_LONG).show()
//     }

    val display = windowManager.defaultDisplay
    val size = Point();
    display.getRealSize(size)
    val width = size.x
    val height = size.y

    val prefs: SharedPreferences = applicationContext.getSharedPreferences("info", Context.MODE_PRIVATE)
    val editor = prefs!!.edit()
    editor.putString("displayWidth", width.toString())
    editor.putString("displayHeight", height.toString())
    editor.apply()



    if (null == savedInstanceState) {
      val prefs: SharedPreferences = applicationContext.getSharedPreferences("info", Context.MODE_PRIVATE)
      if(prefs.getString("cameraIndex", "front").equals("back")) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, Camera2BasicFragment.newInstance())
                .commit()
      }else{
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, Camera2BasicFragment_Back.newInstance())
                .commit()
      }
    }


    textureView = findViewById(R.id.videoTexture)
    textureView.surfaceTextureListener = this
    mediaPlayer = MediaPlayer()

    try {
      fileDescriptor = assets.openFd("video1.mp4")
    }
    catch (e: IOException) {
      e.printStackTrace()
    }


    textureView.setOnClickListener { n ->
      mediaPlayer.pause()

      val imgPlay: ImageView = findViewById(R.id.imgPlay)

      if (imgPlay.getVisibility() == GONE) {
        imgPlay.setVisibility(VISIBLE)
        mediaPlayer.pause()
        imgPlay.setOnClickListener {
          mediaPlayer.start()
          imgPlay.setVisibility(GONE)
        }
      } else {
        imgPlay.setOnClickListener {
        }
      }
    }



  }


  override fun onPause() {
    super.onPause()
    sm?.unregisterListener(this)
    runClassifier = false


    if (mRecognizer != null) {
      mRecognizer!!.destroy()
      mRecognizer!!.cancel()
    }

  }

  override fun onResume() {
    super.onResume()
    runClassifier = true
    if (!OpenCVLoader.initDebug()) {
      OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback)
    } else {
      mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
    }

    sm?.registerListener(
            this,                    // 센서 이벤트 값을 받을 리스너 (현재의 액티비티에서 받음)
            s,     // 센서 종류
            SensorManager.SENSOR_DELAY_UI                             // 수신 빈도
    )

    backgroundThread = HandlerThread(Camera2BasicFragment.HANDLE_THREAD_NAME)
    backgroundThread!!.start()
    backgroundHandler = Handler(backgroundThread!!.getLooper())
    runClassifier = true

    startBackgroundThread(Runnable { classifier!!.initTflite(true) })
    startBackgroundThread(periodicClassify)

    //        Intent intent = new Intent(Main2Activity.this, DialogSpeach.class);
//        startActivityForResult(intent, 1);
    tts = TextToSpeech(this) { status ->
      if (status != TextToSpeech.ERROR) {
        // 언어를 선택한다.
        tts!!.language = Locale.KOREAN
      }
    }


    btnSTTStart = findViewById<Button>(R.id.btnSSTStart)
    btnSTTStart.setOnClickListener(View.OnClickListener { n: View? ->

      val amanager = getSystemService(AUDIO_SERVICE) as AudioManager

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//      amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
        amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0)
//      amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0)
//      amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
//      amanager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0)
//      amanager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0)
      } else {
//      amanager.setStreamMute(AudioManager.STREAM_MUSIC, true)
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true)
//      amanager.setStreamMute(AudioManager.STREAM_ALARM, true)
//      amanager.setStreamMute(AudioManager.STREAM_MUSIC, true)
//      amanager.setStreamMute(AudioManager.STREAM_RING, true)
//      amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true)
      }



      if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
      } else {
        try {
          mRecognizer!!.startListening(SttIntent)
        } catch (e: java.lang.Exception) {
          e.printStackTrace()
        }
      }
    })

    SttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, applicationContext.packageName)
    SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
    mRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
    mRecognizer!!.setRecognitionListener(listener)


//    txtInMsg = findViewById(R.id.txtInMsg);
//    txtSystem = findViewById(R.id.txtSystem);
    Handler().postDelayed({ //                txtSystem.setText("어플 실행됨 자동 실행" + "\r\n" + txtSystem.getText());
      btnSTTStart.performClick()
    }, 1000)


  }

  companion object {

    init {
      //        System.loadLibrary("opencv_java");
      System.loadLibrary("opencv_java3")
    }

    @JvmStatic
    var isOpenCVInit = false
  }



  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(data!=null) {
      if (requestCode == 2) {
        if (resultCode == RESULT_OK) {

          val uri: Uri = data.getData()!!
          val path: String = getPath(uri)
          val name: String = getName(uri)
          val uriId: String = getUriId(uri)
          val realFilePath = getRealPathFromUri(uri)

        }
      }
    }
      if (resultCode == 1001) {
        if(MainActivity.getMain2().startState !=1){
          btnPlay!!.performClick()
        }else{
          Toast.makeText(this, "이미 운동중입니다.", Toast.LENGTH_SHORT).show()
        }

      } else if (resultCode == 1002) {
        if(MainActivity.getMain2().startState ==1) {
          btnPlay!!.performClick()
        }else{
          Toast.makeText(this, "아직 운동이 시작되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
      } else if (resultCode == 1003) {
//        btnPlus!!.performClick()
        MainActivity.getMain2().PlusAllClick()
      } else if (resultCode == 1004) {
//        btnMinus!!.performClick()
        MainActivity.getMain2().MinusAllClick()
      } else {
        tts!!.speak("다시 말씀해주세요", TextToSpeech.QUEUE_FLUSH, null)
//            Toast.makeText(Main2Activity.this, "다시 말씀해주세요", Toast.LENGTH_SHORT).show();
      }


//    }
  }




  fun getRealPathFromUri(contentURI: Uri):String{
    val result : String
    val cursor : Cursor = getContentResolver().query(contentURI, null, null, null, null)!!
    if(cursor == null){
      result = contentURI.getPath()!!
    }else{
      cursor.moveToFirst()
      val idx : Int =cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
      result = cursor.getString(idx)
      cursor.close()
    }
    return result
  }

  fun getPath(uri: Uri):String{
    val projection : Array<String> = arrayOf(MediaStore.Video.Media.DATA)
    val cursor : Cursor =managedQuery(uri, projection, null, null, null)
    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    return cursor.getString(column_index)
  }

  fun getName(uri: Uri): String {
    val projection : Array<String> = arrayOf(MediaStore.Video.VideoColumns.DISPLAY_NAME)
    val cursor : Cursor =managedQuery(uri, projection, null, null, null)
    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DISPLAY_NAME)
    cursor.moveToFirst()
    return cursor.getString(column_index)
  }

  fun getUriId(uri: Uri): String {
    val projection : Array<String> = arrayOf(MediaStore.Video.VideoColumns._ID)
    val cursor : Cursor =managedQuery(uri, projection, null, null, null)
    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns._ID)
    cursor.moveToFirst()
    return cursor.getString(column_index)
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  }
  override fun onSensorChanged(event: SensorEvent?) {

    val btnPlus : Button =findViewById(R.id.btnPlus)
    val btnMinus : Button  =findViewById(R.id.btnMinus)
    val btnPlay : Button = findViewById(R.id.btnPlay)
    val btnPose : Button  = findViewById(R.id.btnPose)
    val btnChange : LinearLayout  = findViewById(R.id.btnChange)
    val videoTexture : TextureView = findViewById(R.id.videoTexture)
    val videoImage : ImageView = findViewById(R.id.imgPlay)
    val videoDrawer : DrawView = findViewById(R.id.videoDrawView)

    if(event?.sensor?.type == Sensor.TYPE_ORIENTATION){

      val str :String = """
        방향센서값
        방위각: ${event.values[0]}
        피치 : ${event.values[1]}
        롤 : ${event.values[2]}
        """.trimIndent()

//            Log.d("str", str)
      if (event.values[2] > 60) {
        btnPlay?.rotation = 90f
        btnPose?.rotation = 90f
        btnChange?.rotation = 90f
        btnPlus?.rotation = 90f
        btnMinus?.rotation = 90f
        videoDrawer?.rotation = 90f
        videoTexture?.rotation = 90f
        videoImage?.rotation = 90f
      }
      if (event.values[2] < 40) {
        btnPlay?.rotation = 0f
        btnPose?.rotation = 0f
        btnChange?.rotation = 0f
        btnPlus?.rotation = 0f
        btnMinus?.rotation = 0f
        videoDrawer?.rotation = 0f
        videoTexture?.rotation =0f
        videoImage?.rotation = 0f
      }

    }

  }



  @Synchronized
  protected fun startBackgroundThread(r: Runnable) {
    if (backgroundHandler != null) {
      backgroundHandler!!.post(r)
    }
  }

  private fun classifyFrame() {
    if(PageOpen ==1) {
      drawView = findViewById(R.id.videoDrawView)
      textureView = findViewById(R.id.videoTexture)


      runOnUiThread {
        val textureView: TextureView = findViewById(R.id.videoTexture)
        drawView!!.setAspectRatio(textureView.height, textureView.width)
      }

      if (classifier == null || this == null) {
        return
      }
      try {
        var bitmap = textureView!!.getBitmap(classifier!!.imageSizeX, classifier!!.imageSizeY)
        val textToShow = classifier!!.classifyFrame(bitmap)
        bitmap.recycle()
        drawView!!.setDrawPoint(classifier!!.mPrintPointArray!!, 0.5f)

      } catch (e: Exception) {
//        finish()
        e.printStackTrace()
      }
    }
  }

  private val periodicClassify = object : Runnable {
    override fun run() {
      synchronized(lock) {
        if (runClassifier) {
//          classifyFrame()
          drawView = findViewById(R.id.videoDrawView)
          textureView = findViewById(R.id.videoTexture)


          runOnUiThread {
            val textureView: TextureView = findViewById(R.id.videoTexture)
            drawView!!.setAspectRatio(textureView.height, textureView.width)
          }

          if (classifier == null || this == null) {
            return
          }
          try {
            var bitmap = textureView!!.getBitmap(classifier!!.imageSizeX, classifier!!.imageSizeY)
            val textToShow = classifier!!.classifyFrame(bitmap)
            bitmap.recycle()
            drawView!!.setDrawPoint(classifier!!.mPrintPointArray!!, 0.5f)

          } catch (e: Exception) {
            finish()
          }
        }
      }
      backgroundHandler!!.post(this)
    }
  }
  override fun onDestroy() {
    classifier!!.close()
    super.onDestroy()
    if (tts != null) {
      tts!!.stop()
      tts!!.shutdown()
      tts = null
    }
    if (mRecognizer != null) {
      mRecognizer!!.destroy()
      mRecognizer!!.cancel()
      mRecognizer = null
    }
  }

  override fun onBackPressed() {
    runClassifier = false
    val prefs: SharedPreferences = applicationContext.getSharedPreferences("info", Context.MODE_PRIVATE)
/*    if(prefs.getString("cameraIndex", "front").equals("back")) {
      val camera2BasicFragment = Camera2BasicFragment()
      camera2BasicFragment.detachFragMent()
    }else{
      val cameraCamera2BasicFragment_Back = Camera2BasicFragment_Back()
      cameraCamera2BasicFragment_Back.detachFragMent()
    }*/
//    drawView!!.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
    val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
    val camera2BasicFragment_null = Camera2BasicFragment_null()
//
    fragmentTransaction.replace(R.id.container, camera2BasicFragment_null).commit()
    sm!!.unregisterListener(this)
    mediaPlayer.stop()
//
//    val intent = Intent(this, MainActivity::class.java)
//    startActivity(intent)
//    drawView!!.destroyDrawingCache()
//    finish()
    super.onBackPressed()
//    val handler : Handler = Handler()
//    handler.postDelayed(Runnable {
//      finish()
//    },1000)
//
  }

  override fun onRequestPermissionsResult(
          requestCode: Int,
          permissions: Array<String>,
          grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }



  private var backgroundThread: HandlerThread? = null
  private var backgroundHandler: Handler? = null
  private var previewSize: Size? = null




  override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {

    val context : Context = applicationContext
    val drawable : Drawable? = context.getDrawable(R.drawable.imageview_rounding)
    val thumbnail1 : ImageView = findViewById(R.id.thumb_nail1)
    thumbnail1.background = drawable
    thumbnail1.clipToOutline=true
    thumbnail1.setImageDrawable(resources.getDrawable(R.drawable.thumb_nail1))

    val thumbnail2 : ImageView = findViewById(R.id.thumb_nail2)
    thumbnail2.setImageDrawable(resources.getDrawable(R.drawable.thumb_nail2))
    thumbnail2.background = drawable
    thumbnail2.clipToOutline=true

    val thumbnailLayout : LinearLayout = findViewById(R.id.thumb_nail_layout)
    thumbnail1.setOnClickListener{ n->
      mediaPlayer.reset()
      thumbnailLayout.visibility = GONE
      try {
        fileDescriptor = assets.openFd("video1.mp4")
      } catch (e: IOException) {
        e.printStackTrace()
      }
      val surface = Surface(surfaceTexture)

      try {
        mediaPlayer.setSurface(surface)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          mediaPlayer.setDataSource(fileDescriptor)
          mediaPlayer.prepareAsync()
          mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            mediaPlayer.pause()
            val imgPlay: ImageView = findViewById(R.id.imgPlay)
            imgPlay.setVisibility(VISIBLE)
            imgPlay.setOnClickListener { n ->
              mediaPlayer.start()
              imgPlay.setVisibility(GONE)
            }
          }
          mediaPlayer.setOnCompletionListener {

            thumbnailLayout.visibility = VISIBLE

            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            mediaPlayer.pause()

            val imgPlay: ImageView = findViewById(R.id.imgPlay)
            imgPlay.setVisibility(VISIBLE)
            imgPlay.setOnClickListener { n ->
              mediaPlayer.start()
              imgPlay.setVisibility(GONE)
            }
          }
        }

      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
    thumbnail2.setOnClickListener{ n->
      mediaPlayer.reset()
      thumbnailLayout.visibility = GONE

      try {
        fileDescriptor = assets.openFd("video2.mp4")
      } catch (e: IOException) {
        e.printStackTrace()
      }
      val surface = Surface(surfaceTexture)

      try {
        mediaPlayer.setSurface(surface)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          mediaPlayer.setDataSource(fileDescriptor)
          mediaPlayer.prepareAsync()
          mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            mediaPlayer.pause()
            val imgPlay: ImageView = findViewById(R.id.imgPlay)
            imgPlay.setVisibility(VISIBLE)
            imgPlay.setOnClickListener { n ->
              mediaPlayer.start()
              imgPlay.setVisibility(GONE)
            }
          }
          mediaPlayer.setOnCompletionListener {

            thumbnailLayout.visibility = VISIBLE

            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            mediaPlayer.pause()

            val imgPlay: ImageView = findViewById(R.id.imgPlay)
            imgPlay.setVisibility(VISIBLE)
            imgPlay.setOnClickListener { n ->
              mediaPlayer.start()
              imgPlay.setVisibility(GONE)

            }
          }
        }

      } catch (e: IOException) {
        e.printStackTrace()
      }
    }




  }
  override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
  override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
    return false
  }
  override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
  }
  override fun onVideoSizeChanged(mp: MediaPlayer, width: Int, height: Int) {}



//  object getMain: AppCompatActivity() {
//      fun getMain() :CameraActivity{
//        return CameraActivity()
//    }
//  }

  object asd : CameraActivity(){
    fun setText(state: Boolean){
//      Log.d("state", state.toString())
      if(state == true) {
        text?.visibility = GONE
      }else{
        text?.visibility = GONE
      }
    }
  }

  private val listener: RecognitionListener = object : RecognitionListener {
    override fun onReadyForSpeech(params: Bundle) {
//            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
    }

    override fun onBeginningOfSpeech() {
//            txtSystem.setText("인식 시작.");
//            Toast.makeText(Main2Activity.this, "인식시작", Toast.LENGTH_SHORT).show();
    }

    override fun onRmsChanged(rmsdB: Float) {
//            txtSystem.setText("Rms Changed");
//            Toast.makeText(Main2Activity.this, "Rms Changed", Toast.LENGTH_SHORT).show();
    }

    override fun onBufferReceived(buffer: ByteArray) {
//            txtSystem.setText("onBufferReceived..........."+"\r\n"+txtSystem.getText());
    }

    override fun onEndOfSpeech() {
//            txtSystem.setText("인식끝");
//            Toast.makeText(Main2Activity.this, "인식끝", Toast.LENGTH_SHORT).show();
      btnSTTStart.performClick()
    }

    override fun onError(error: Int) {
      val message: String
      when (error) {
        SpeechRecognizer.ERROR_AUDIO -> message = "오디오 에러"
        SpeechRecognizer.ERROR_CLIENT -> message = "클라이언트 에러"
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> message = "퍼미션 없음"
        SpeechRecognizer.ERROR_NETWORK -> message = "네트워크 에러"
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> message = "네트웍 타임아웃"
        SpeechRecognizer.ERROR_NO_MATCH -> {
          message = "찾을 수 없음"
          btnSTTStart.performClick()
        }
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
          message = "RECOGNIZER가 바쁨"
          btnSTTStart.performClick()
        }
        SpeechRecognizer.ERROR_SERVER -> message = "서버가 이상함"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
          message = "말하는 시간초과"
          btnSTTStart.performClick()
        }
        else -> message = "알 수 없는 오류임"
      }
    }

    override fun onResults(results: Bundle) {
      var key = ""
      key = SpeechRecognizer.RESULTS_RECOGNITION
      val mResult = results.getStringArrayList(key)
      val rs = arrayOfNulls<String>(mResult!!.size)
      mResult.toArray(rs)
      //            txtInMsg.setText(rs[0]+"\r\n"+txtInMsg.getText());
//            Toast.makeText(Main2Activity.this, rs[0], Toast.LENGTH_SHORT).show();
      rs[0]?.let { FuncVoiceOrderCheck(it) }
      mRecognizer!!.startListening(SttIntent)
    }

    override fun onPartialResults(partialResults: Bundle) {}
    override fun onEvent(eventType: Int, params: Bundle) {}
  }


  fun FuncVoiceOrderCheck(VoiceMsg: String) {
    var VoiceMsg = VoiceMsg
    if (VoiceMsg.length < 1) return
    VoiceMsg = VoiceMsg.replace(" ", "") //공백제거

//        if(VoiceMsg.indexOf("카카오톡")>-1 || VoiceMsg.indexOf("카톡")>-1){
//            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.kakao.talk");
//            startActivity(launchIntent);
//            onDestroy();
//        }
    Log.d("Voice", VoiceMsg)
    if (VoiceMsg.contains("빅스비")) {
      val intent: Intent = Intent(this, DialogSpeach::class.java)
      startActivityForResult(intent, 1)
//      tts!!.speak("하이 빅스비", TextToSpeech.QUEUE_FLUSH, null)
    }
  }

}
