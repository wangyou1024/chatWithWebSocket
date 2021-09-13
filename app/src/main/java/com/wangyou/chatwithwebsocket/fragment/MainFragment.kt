package com.wangyou.chatwithwebsocket.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wangyou.chatwithwebsocket.R
import com.wangyou.chatwithwebsocket.data.MainUIViewModel
import com.wangyou.chatwithwebsocket.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    private var mainUIViewModel:  MainUIViewModel? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<FragmentMainBinding>(inflater, R.layout.fragment_main, container, false)
        binding!!.lifecycleOwner = this
        mainUIViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(MainUIViewModel::class.java)
        setIcon(-1)
        when(mainUIViewModel!!.getPage()){
            0 -> binding!!.session.setImageResource(R.mipmap.session_selected)
            1 -> binding!!.address.setImageResource(R.mipmap.address_selected)
            else -> binding!!.personal.setImageResource(R.mipmap.personal_selected)
        }
        // 无法直接通过binding拿到fragment元素，只有使用原始的方法去获取
        val fragmentContainer = binding!!.root.findViewById<View>(R.id.fragmentMain)
        navController = Navigation.findNavController(fragmentContainer)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding!!.session.setOnClickListener {
            if(!navController!!.popBackStack(R.id.sessionFragment, true)){
                navController!!.navigate(R.id.sessionFragment)
            }
            setIcon(0)
            mainUIViewModel!!.setPage(0)
            getIconAnimator(binding!!.session, R.mipmap.session, R.mipmap.session_selected).start();
        }
        binding!!.address.setOnClickListener {
            if(!navController!!.popBackStack(R.id.addressFragment, false)){
                navController!!.navigate(R.id.addressFragment)
            }
            setIcon(1)
            mainUIViewModel!!.setPage(1)
            getIconAnimator(binding!!.address, R.mipmap.address, R.mipmap.address_selected).start()
        }
        binding!!.personal.setOnClickListener {
            if(!navController!!.popBackStack(R.id.personalFragment, false)){
                navController!!.navigate(R.id.personalFragment)
            }
            setIcon(2)
            mainUIViewModel!!.setPage(2)
            getIconAnimator(binding!!.personal, R.mipmap.personal, R.mipmap.personal_selected).start()
        }
    }


    @SuppressLint("ObjectAnimatorBinding")
    private fun getIconAnimator(icon: ImageView, oldIcon: Int, newIcon: Int): AnimatorSet{
        val animatorSetReduce = AnimatorSet()
        var scaleY = ObjectAnimator.ofFloat(icon, "scaleY", 1f, 0.5f)
        var scaleX = ObjectAnimator.ofFloat(icon, "scaleX", 1f, 0.5f)
        animatorSetReduce.playTogether(scaleX,scaleY)
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

    private fun setIcon(page: Int){
        if (page != mainUIViewModel!!.getPage()){
            binding!!.session.setImageResource(R.mipmap.session)
            binding!!.address.setImageResource(R.mipmap.address)
            binding!!.personal.setImageResource(R.mipmap.personal)
        }
    }


}