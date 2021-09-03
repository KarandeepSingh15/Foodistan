package com.karandeepsingh.foodistan.fragments

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.adapter.QuestionsAdapter
import com.karandeepsingh.foodistan.model.Question


class FAQ : Fragment() {
    lateinit var recyclerQuestion: RecyclerView
    lateinit var recyclerLayoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: QuestionsAdapter
    var questionList = listOf<Question>(
        Question(
            "Which Payment Methods Can We Use?",
            "We accept payment through e-wallets,credit/debit cards and cash. "
        ),
        Question(
            "What if I don't get fresh food?",
            "No worries!! You just send us a pic of spoilt food and we will contact the restaurant and will get you another order or a refund."
        ),
        Question("Which Restaurants Can I choose from?",
        "You Can order from all the nearby restaurants and food chains which have registered with us.")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_faq, container, false)
        recyclerQuestion=view.findViewById(R.id.recyclerQuestion)
        recyclerLayoutManager=LinearLayoutManager(activity as Context)
        recyclerQuestion.layoutManager=recyclerLayoutManager
        recyclerAdapter= QuestionsAdapter(activity as Context,questionList)
        recyclerQuestion.adapter=recyclerAdapter
        recyclerQuestion.addItemDecoration(
            DividerItemDecoration(activity as Context,(recyclerLayoutManager as LinearLayoutManager).orientation
        ))
        return view
    }


}