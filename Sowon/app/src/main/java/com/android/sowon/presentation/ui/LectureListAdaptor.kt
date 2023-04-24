package com.android.sowon.presentation.ui

import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.sowon.data.retrofit.model.Lecture
import com.android.sowon.databinding.LecturelistItemBinding

class LectureListAdaptor(private val screenWidth: Int) :
    ListAdapter<Lecture, LectureListAdaptor.LectureItemViewHolder>(diffUtil) {

    // 목록에 있는 개별 항목의 레이아웃을 포함하는 View의 래퍼
    inner class LectureItemViewHolder(binding: LecturelistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.image
        private val title = binding.title
        private val type = binding.type
        private val lecturer = binding.lecturer
        private val border = binding.border
        private val info = binding.info

        fun bind(lecture: Lecture) {
            image.setImageResource(lecture.image)
            title.text = lecture.title
            type.text = lecture.type
            lecturer.text = lecture.lecturer

            // text size
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 16.toFloat())
            type.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 20.toFloat())
            lecturer.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 20.toFloat())
            border.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenWidth / 20.toFloat())

            // marginTop, marginLeft 세팅
            // title
            val layoutParams_title = title.layoutParams as ConstraintLayout.LayoutParams // Get the current LayoutParams and cast them to ConstraintLayout.LayoutParams
            val newMarginTop1 = screenWidth / 25 // Replace with your desired value
            layoutParams_title.topMargin = newMarginTop1
            layoutParams_title.leftMargin = screenWidth / 90
            title.layoutParams = layoutParams_title  // Set the modified LayoutParams back to the view
            // 기타 정보
            val layoutParams_info = info.layoutParams as ConstraintLayout.LayoutParams // Get the current LayoutParams and cast them to ConstraintLayout.LayoutParams
            val newMarginTop = screenWidth / 90 // Replace with your desired value
            layoutParams_info.topMargin = newMarginTop
            layoutParams_info.leftMargin = screenWidth / 90
            info.layoutParams = layoutParams_info  // Set the modified LayoutParams back to the view

            // 아이템 클릭 이벤트 리스너 추가 - onBindViewHolder에서 등록하면 해당 아이템이 그려질 때마다 클릭 이벤트 리스너가 등록되므로 메모리 소비가 증가할 수 있다.
            // 반면, ViewHolder에 클릭 이벤트가 등록되면, 해당 뷰홀더가 생성될 때 한 번만 등록하면 되므로, 메모리 소비가 더 적다.
            itemView.setOnClickListener { // itemView는 RecyclerView.ViewHolder 클래스에 정의된 프로퍼티로, 이 클래스가 리사이클러뷰에서 각각의 아이템 뷰를 관리할 때 사용하는 뷰 객체를 참조한다. 따라서 이 코드에서 itemView는 LectureItemViewHolder가 관리하는 뷰 객체를 참조하는 것이다. 클릭 이벤트 리스너를 추가할 때는 해당 뷰 객체인 itemView를 사용하여 클릭 이벤트를 처리하도록 코드를 작성한다.
                val intent = Intent(itemView.context, LectureActivity::class.java)
                intent.putExtra("lecture", lecture)
                itemView.context.startActivity(intent)
            }
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

    // ViewHolder를 데이터와 연결
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
