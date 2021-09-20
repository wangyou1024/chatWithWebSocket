package com.wangyou.chatwithwebsocket

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.LoginViewModel
import com.wangyou.chatwithwebsocket.data.PersonalViewModel
import com.wangyou.chatwithwebsocket.databinding.ActivityMainBinding
import com.wangyou.chatwithwebsocket.net.api.FileServiceAPI
import com.wangyou.chatwithwebsocket.net.client.StompClientLifecycle
import com.wangyou.chatwithwebsocket.net.response.CompositeDisposableLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ua.naiksoftware.stomp.StompClient
import javax.inject.Inject

import android.provider.MediaStore

import android.content.Intent
import android.text.TextUtils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.FileUtils
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.wangyou.chatwithwebsocket.data.GroupDetailViewModel
import com.wangyou.chatwithwebsocket.data.MainUIViewModel
import com.wangyou.chatwithwebsocket.net.exception.APIException
import com.wangyou.chatwithwebsocket.net.exception.ErrorConsumer
import com.wangyou.chatwithwebsocket.net.response.ResponseTransformer
import okhttp3.*
import java.io.File


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var toast: Toast
    @Inject lateinit var stompClient: StompClient
    @Inject lateinit var okHttpClient: OkHttpClient
    @Inject lateinit var fileServiceAPI: FileServiceAPI
    @Inject lateinit var stompClientLifecycle: StompClientLifecycle
    @Inject lateinit var compositeDisposableLifecycle: CompositeDisposableLifecycle

    private val personalViewModel by viewModels<PersonalViewModel>()
    private val groupViewModel by viewModels<GroupDetailViewModel>()
    private val mainUIViewModel by viewModels<MainUIViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()

    private var navController: NavController? = null
    private var binding: ActivityMainBinding? = null
    private var launcher = registerForActivityResult(AlbumContract(), ActivityResultCallback {
        Log.i(Const.TAG, "获取图片 -> $it")
    })

    companion object {
        private const val ALBUM_CODE = 100
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.fragmentAll)
        loginStatus()
        lifecycle.addObserver(compositeDisposableLifecycle)
        lifecycle.addObserver(stompClientLifecycle)
    }

    private fun loginStatus(){
        // 查看本地cookie状态
        val cookieList = okHttpClient.cookieJar().loadForRequest(HttpUrl.get(Const.address))
        personalViewModel.loadSelfError.observe(this, {
            if (it) {
                loginPage("登录状态异常")
            } else {
                personalViewModel.getPersonal().value?.username?.let { username ->
                    loginViewModel.setUsername(username)
                }
            }
        })
        loginViewModel.getLogined().observe(this, {
            Log.i(Const.TAG, "登录状态更新$it")
            if (it){
                personalViewModel.loadSelf()
                if(!stompClient.isConnected){
                    Log.i(Const.TAG, "重新连接stompClient")
                    stompClientLifecycle.connect()
                }
            }
        })
        when {
            cookieList.isEmpty() -> {
                loginPage("未登录")
            }
            cookieList[0].expiresAt() < System.currentTimeMillis() -> {
                loginPage("登录过期")
            }
            else -> {
                personalViewModel.loadSelf()
            }
        }
    }

    /**
     * 个人中心的退出按钮
     */
    fun exit(view: View) {
        loginViewModel.logout()
        SharedPrefsCookiePersistor(this).clear()
        loginPage("已退出")
    }

    /**
     * 托管fragment的返回
     */
    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.fragmentAll).navigateUp()

    private fun loginPage(error: String) {
        navController!!.popBackStack(R.id.mainFragment, true)
        navController!!.navigate(R.id.loginFragment)
        toast.duration = Toast.LENGTH_SHORT
        toast.setText(error)
        toast.show()
    }

    private fun openAlbum(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        launcher.launch(true)
    }

    fun checkPermissionForUpload(view: View){
        when(view.id){
            R.id.groupHeader -> mainUIViewModel.setHeaderType(1)
            R.id.personalHeader -> mainUIViewModel.setHeaderType(0)
        }
        Log.i(Const.TAG, "上传头像 -> ${mainUIViewModel.getHeaderType()}")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
         == PackageManager.PERMISSION_GRANTED) {
            openAlbum();
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), ALBUM_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE){
            openAlbum()
        }
    }


    private fun getFileFromUri(uri: Uri?, context: Context): File? {
        return if (uri == null) {
            null
        } else when (uri.scheme) {
            "content" -> getFileFromContentUri(uri, context)
            "file" -> File(uri.path!!)
            else -> null
        }
    }

    /**
     * 通过内容解析中查询uri中的文件路径
     */
    private fun getFileFromContentUri(contentUri: Uri?, context: Context): File? {
        if (contentUri == null) {
            return null
        }
        var file: File? = null
        val filePath: String
        val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor: Cursor? = context.contentResolver.query(
            contentUri, filePathColumn, null,
            null, null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]))
            Log.i(Const.TAG, "文件路径 -> $filePath")
            cursor.close()
            if (!TextUtils.isEmpty(filePath)) {
                file = File(filePath)
            }
        }
        return file
    }

    private fun updateFile(file: File){
        val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val part = MultipartBody.Part.createFormData("imageFile", file.name, requestBody)
        val id = when(mainUIViewModel.getHeaderType()){
            0 -> personalViewModel.getSelf().value?.uid
            1 -> groupViewModel.getGroup().value?.gid
            else -> -1
        }
        fileServiceAPI.uploadImage(part, mainUIViewModel.getHeaderType(), id!!)
            .compose(ResponseTransformer.option(compositeDisposableLifecycle.compositeDisposable))
            .subscribe({
                toast.setText(it)
                toast.show()
            }, object : ErrorConsumer() {
                override fun error(ex: APIException) {
                    Log.i(Const.TAG, "上传 -> ${ex.errorMsg}")
                    toast.setText("上传异常")
                    toast.show()
                }
            })
    }

    inner class AlbumContract : ActivityResultContract<Boolean, String>(){
        override fun createIntent(context: Context, input: Boolean?): Intent {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): String {
            if (resultCode == RESULT_OK) {
                val imageUri: Uri? = intent?.data
                //处理uri,7.0以后的fileProvider 把URI 以content provider 方式 对外提供的解析方法
                val file: File? = getFileFromUri(imageUri, applicationContext)
                if (file?.exists() == true) {
                    updateFile(file)
                }
            }
            return "success"
        }
    }
}