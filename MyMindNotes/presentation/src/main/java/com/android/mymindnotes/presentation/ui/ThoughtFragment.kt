package com.android.mymindnotes.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.mymindnotes.presentation.databinding.ActivityThoughtFragmentBinding
import com.android.mymindnotes.core.model.UserDiary
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ThoughtFragment @Inject constructor(

) : Fragment() {
    private lateinit var binding: ActivityThoughtFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityThoughtFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.content.text = arguments?.getString("thought")
    }

    fun refreshData(diary: UserDiary) {
        binding.content.text = diary.thought
    }

}