package com.sd.elevenballs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sd.elevenballs.R
import com.sd.elevenballs.databinding.BallItemBinding
import com.sd.elevenballs.dto.Ball


class BallAdapter:RecyclerView.Adapter<BallAdapter.BallHolder>() {

    val ballList = ArrayList<Ball>()

    class BallHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = BallItemBinding.bind(item)

        fun bind(ball: Ball) = with(binding) {
            imBall.setImageResource(ball.colorBall)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BallHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ball_item, parent, false)
        return BallHolder(view)
    }

    override fun onBindViewHolder(holder: BallHolder, position: Int) {
        holder.bind(ballList[position])
    }

    override fun getItemCount(): Int {
        return ballList.size
    }
    fun addBall(ball: Ball) {
        ballList.add(ball)
        notifyDataSetChanged()
    }

    fun removeBall() {
        ballList.removeLast()
        notifyDataSetChanged()
    }

}