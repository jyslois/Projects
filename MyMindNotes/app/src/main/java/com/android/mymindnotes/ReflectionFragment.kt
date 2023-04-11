package com.android.mymindnotes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.mymindnotes.data.retrofit.model.diary.UserDiary
import com.android.mymindnotes.ReflectionFragment
import com.android.mymindnotes.databinding.ActivityReflectionFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReflectionFragment @Inject constructor(

) : Fragment() {
    private lateinit var binding: ActivityReflectionFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityReflectionFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.content.text = arguments?.getString("reflection")
    }

    fun refreshData(diary: UserDiary) {
        binding.content.text = diary.reflection
    }

    companion object {
        fun newInstance(bundle: Bundle?): ReflectionFragment {
            val fragment = ReflectionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}