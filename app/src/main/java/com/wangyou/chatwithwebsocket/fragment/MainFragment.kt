package com.wangyou.chatwithwebsocket.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.data.MainUIViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment(), LifecycleObserver {

    private var binding: FragmentMainBinding? = null
    private val mainUIViewModel by activityViewModels<MainUIViewModel>()
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentMainBinding>(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        binding!!.lifecycleOwner = this
        setClearIcon(-1)
        when (mainUIViewModel.getPage()) {
            0 -> binding!!.session.setImageResource(R.mipmap.session_selected)
            1 -> binding!!.address.setImageResource(R.mipmap.address_selected)
            else -> binding!!.personal.setImageResource(R.mipmap.personal_selected)
        }
        // 无法直接通过binding拿到fragment元素，只有使用原始的方法去获取
        val fragmentContainer = binding!!.root.findViewById<View>(R.id.fragmentMain)
        navController = Navigation.findNavController(fragmentContainer)
        return binding!!.root
    }

    override fun onActivityResume(){
        Log.i(Const.TAG, "onCreated")
        binding!!.session.setOnClickListener { sessionPage() }
        binding!!.sessionLabel.setOnClickListener { sessionPage() }
        binding!!.address.setOnClickListener { addressPage() }
        binding!!.addressLabel.setOnClickListener { addressPage() }
        binding!!.personal.setOnClickListener { personalPage() }
        binding!!.personalLabel.setOnClickListener { personalPage() }
    }


    @SuppressLint("ObjectAnimatorBinding")
    private fun getIconAnimator(icon: ImageView, oldIcon: Int, newIcon: Int): AnimatorSet {
        val animatorSetReduce = AnimatorSet()
        var scaleY = ObjectAnimator.ofFloat(icon, "scaleY", 1f, 0.5f)
        var scaleX = ObjectAnimator.ofFloat(icon, "scaleX", 1f, 0.5f)
        animatorSetReduce.playTogether(scaleX, scaleY)
        animatorSetReduce.duration = 200
        val animatorResource = ObjectAnimator.ofInt(icon, "imageResource", oldIcon, newIcon)
        animatorResource.duration = 0
        val animatorSetBoost = AnimatorSet()
        scaleY = ObjectAnimator.ofFloat(icon, "scaleY", 0.5f, 1f)
        scaleX = ObjectAnimator.ofFloat(icon, "scaleX", 0.5f, 1f)
        animatorSetBoost.playTogether(scaleX, scaleY)
        animatorSetBoost.duration = 200
        val animatorMain = AnimatorSet()
        animatorMain.playSequentially(animatorSetReduce, animatorResource, animatorSetBoost)
        return animatorMain
    }

    private fun setClearIcon(page: Int) {
        if (page != mainUIViewModel.getPage()) {
            binding!!.session.setImageResource(R.mipmap.session)
            binding!!.sessionLabel.setTextColor(resources.getColor(R.color.gray_400, null))
            binding!!.address.setImageResource(R.mipmap.address)
            binding!!.addressLabel.setTextColor(resources.getColor(R.color.gray_400, null))
            binding!!.personal.setImageResource(R.mipmap.personal)
            binding!!.personalLabel.setTextColor(resources.getColor(R.color.gray_400, null))
            when (page) {
                0 -> binding!!.sessionLabel.setTextColor(resources.getColor(R.color.blue_400, null))
                1 -> binding!!.addressLabel.setTextColor(resources.getColor(R.color.blue_400, null))
                2 -> binding!!.personalLabel.setTextColor(resources.getColor(R.color.blue_400, null))
                else -> return
            }
        }
    }

    private fun sessionPage() {
        if (!navController!!.popBackStack(R.id.sessionFragment, true)) {
            navController!!.navigate(R.id.sessionFragment)
        }
        setClearIcon(0)
        mainUIViewModel.setPage(0)
        getIconAnimator(binding!!.session, R.mipmap.session, R.mipmap.session_selected).start();
    }

    private fun addressPage() {
        if (!navController!!.popBackStack(R.id.addressFragment, false)) {
            navController!!.navigate(R.id.addressFragment)
        }
        setClearIcon(1)
        mainUIViewModel.setPage(1)
        getIconAnimator(binding!!.address, R.mipmap.address, R.mipmap.address_selected).start()
    }

    private fun personalPage() {
        if (!navController!!.popBackStack(R.id.personalFragment, false)) {
            navController!!.navigate(R.id.personalFragment)
        }
        setClearIcon(2)
        mainUIViewModel.setPage(2)
        getIconAnimator(binding!!.personal, R.mipmap.personal, R.mipmap.personal_selected).start()
    }

}