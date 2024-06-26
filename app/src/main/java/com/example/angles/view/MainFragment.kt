package com.example.angles.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.angles.databinding.FragmentMainBinding
import com.example.angles.viewmodel.MainViewModel


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by lazy { ViewModelProvider(requireActivity())[MainViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCon().observe(viewLifecycleOwner, Observer {
            binding.conBtn.text = if (it) "Connected" else "NotConnected"
        })
        viewModel.getOrient().observe(viewLifecycleOwner, Observer { it?.let {
            binding.pitchValue.text = String.format("%.2f", it.pitch)
            binding.rollValue.text = String.format("%.2f", it.roll)
            binding.yawValue.text = String.format("%.2f", it.yaw)
            binding.altValue.text = String.format("%.2f", it.alt)
        } })
//        binding.conBtn

    }

//    companion object {
//        @JvmStatic
//        fun newInstance() = MainFragment()
//    }
}