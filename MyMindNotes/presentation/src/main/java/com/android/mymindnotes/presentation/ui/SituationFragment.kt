package com.android.mymindnotes.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.mymindnotes.presentation.databinding.ActivitySituationFragmentBinding
import com.android.mymindnotes.core.model.UserDiary
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SituationFragment @Inject constructor(

) : Fragment() {
    lateinit var binding: ActivitySituationFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivitySituationFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.content.text = arguments?.getString("situation")
    }

    fun refreshData(diary: UserDiary) {
        binding.content.text = diary.situation
    }

}