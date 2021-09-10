package com.wangyou.chatwithwebsocket.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentMainBinding>(inflater, R.layout.fragment_main, container, false)
        binding!!.lifecycleOwner = this
        // 无法直接通过binding拿到fragment元素，只有使用原始的方法去获取
        val fragmentContainer = binding!!.root.findViewById<View>(R.id.fragmentMain)
        val navController = Navigation.findNavController(fragmentContainer)
        binding!!.message.setImageResource(R.mipmap.message_selected)
        binding!!.message.setOnClickListener {
            if(!navController.popBackStack(R.id.messageFragment, true)){
                navController.navigate(R.id.messageFragment)
            }
            getIconAnimator(binding!!.message, R.mipmap.message, R.mipmap.message_selected).start();
        }
        binding!!.address.setOnClickListener {
            if(!navController.popBackStack(R.id.addressFragment, false)){
                navController.navigate(R.id.addressFragment)
            }
            getIconAnimator(binding!!.address, R.mipmap.address, R.mipmap.address_selected).start()
        }
        binding!!.personal.setOnClickListener {
            if(!navController.popBackStack(R.id.personalFragment, false)){
                navController.navigate(R.id.personalFragment)
            }
            getIconAnimator(binding!!.personal, R.mipmap.personal, R.mipmap.personal_selected).start()
        }
        return binding!!.root
    }


    @SuppressLint("ObjectAnimatorBinding")
    private fun getIconAnimator(icon: ImageView, oldIcon: Int, newIcon: Int): AnimatorSet{
        setClearIcon()
        var animatorSetReduce = AnimatorSet()
        var scaleY = ObjectAnimator.ofFloat(icon, "scaleY", 1f, 0.5f)
        var scaleX = ObjectAnimator.ofFloat(icon, "scaleX", 1f, 0.5f)
        animatorSetReduce.playTogether(scaleX,scaleY)
        animatorSetReduce.duration = 200
        var animatorResource = ObjectAnimator.ofInt(icon, "imageResource", oldIcon, newIcon)
        animatorResource.duration = 0
        var animatorSetBoost = AnimatorSet()
        scaleY = ObjectAnimator.ofFloat(icon, "scaleY", 0.5f, 1f)
        scaleX = ObjectAnimator.ofFloat(icon, "scaleX", 0.5f, 1f)
        animatorSetBoost.playTogether(scaleX, scaleY)
        animatorSetBoost.duration = 200
        var animatorMain = AnimatorSet()
        animatorMain.playSequentially(animatorSetReduce, animatorResource, animatorSetBoost)
        return animatorMain
    }

    private fun setClearIcon(){
        binding!!.message.setImageResource(R.mipmap.message)
        binding!!.address.setImageResource(R.mipmap.address)
        binding!!.personal.setImageResource(R.mipmap.personal)
    }
}