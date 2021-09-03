package com.karandeepsingh.foodistan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.model.Question

class QuestionsAdapter(val context: Context,val questionList:List<Question>):RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {
   class QuestionViewHolder(view: View):RecyclerView.ViewHolder(view)
   {
       val txtQuestion:TextView=view.findViewById(R.id.txtQuestion)
       val txtAnswer:TextView=view.findViewById(R.id.txtAnswer)
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.faq_single_item,parent,false)
        return QuestionViewHolder(view)
    }


    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question=questionList[position]
        holder.txtQuestion.text=question.question
        holder.txtAnswer.text=question.answer
    }

    override fun getItemCount(): Int {
        return questionList.size
    }
}