package kr.ac.beni.beniprj.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.ac.beni.beniprj.databinding.Page2FragmentBinding
import kr.ac.beni.beniprj.viewmodel.VFViewModel

class Page2Fragment : Fragment() {
    private lateinit var mVFViewModel: VFViewModel
    private lateinit var _binding:Page2FragmentBinding
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mVFViewModel = ViewModelProvider(this)[VFViewModel::class.java]
        _binding = Page2FragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        setUIEvent()
    }

    private fun setUIEvent() {

    }

    private fun initView() {

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}