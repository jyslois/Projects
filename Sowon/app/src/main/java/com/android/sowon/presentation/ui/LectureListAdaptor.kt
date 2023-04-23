package com.android.sowon.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.sowon.data.retrofit.model.Lecture
import com.android.sowon.databinding.LecturelistItemBinding

class LectureListAdaptor() :
    ListAdapter<Lecture, LectureListAdaptor.LectureItemViewHolder>(diffUtil) {

    // 목록에 있는 개별 항목의 레이아웃을 포함하는 View의 래퍼
    inner class LectureItemViewHolder(binding: LecturelistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.image
        private val title = binding.title
        private val type = binding.type
        private val lecturer = binding.lecturer
        private val border = binding.border

        fun bind(lecture: Lecture) {
            image.setImageResource(lecture.image)
            title.text = lecture.title
            type.text = lecture.type
            lecturer.text = lecture.lecturer
        }
    }

    // ViewHolder와 그에 연결된 View를 생성하고 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureItemViewHolder {
        return LectureItemViewHolder(
            LecturelistItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // ViewHolder를 데이터와 연ㄷ결
    override fun onBindViewHolder(holder: LectureItemViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Lecture>() {
            override fun areItemsTheSame(oldItem: Lecture, newItem: Lecture): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Lecture, newItem: Lecture): Boolean {
                return oldItem == newItem
            }
        }
    }

}
