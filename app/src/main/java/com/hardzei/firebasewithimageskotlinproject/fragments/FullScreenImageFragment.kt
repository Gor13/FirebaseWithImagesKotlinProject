package com.hardzei.firebasewithimageskotlinproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.hardzei.firebasewithimageskotlinproject.R
import kotlinx.android.synthetic.main.fragment_full_screen_image.*

class FullScreenImageFragment : Fragment() {

    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            url = it.getString(ARG_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_full_screen_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(url).into(detailIV)
        initListeners()

        val scale = requireContext().resources.displayMetrics.density
        detailIV.cameraDistance = DISTANCE_MARGIN * scale
    }

    private fun initListeners() {
        view?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    companion object {
        private const val ARG_URL = "Url"
        private const val DISTANCE_MARGIN = 8000

        @JvmStatic
        fun newInstance(url: String) =
            FullScreenImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, url)
                }
            }
    }
}
