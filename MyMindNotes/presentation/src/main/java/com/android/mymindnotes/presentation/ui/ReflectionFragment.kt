package com.android.mymindnotes.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.mymindnotes.core.model.Diary
import com.android.mymindnotes.presentation.databinding.ActivityReflectionFragmentBinding
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

    fun refreshData(diary: Diary) {
        binding.content.text = diary.reflection
    }

}