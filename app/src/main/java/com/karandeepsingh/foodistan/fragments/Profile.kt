package com.karandeepsingh.foodistan.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.karandeepsingh.foodistan.R


class Profile : Fragment() {
    lateinit var txtUserName:TextView
    lateinit var txtUserMobile:TextView
    lateinit var txtUserEmail:TextView
    lateinit var txtUserAddress:TextView
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
        txtUserName=view.findViewById(R.id.txtUserName)
        txtUserMobile=view.findViewById(R.id.txtUserMobile)
        txtUserEmail=view.findViewById(R.id.txtUserEmail)
        txtUserAddress=view.findViewById(R.id.txtUserAddress)
        if(activity!=null) {
            sharedPreferences = activity?.getSharedPreferences(
                getString(R.string.preference_file_name),
                Context.MODE_PRIVATE
            ) as SharedPreferences
            txtUserName.text=sharedPreferences.getString("name","User")
            txtUserMobile.text=sharedPreferences.getString("mobile_number","")
            txtUserEmail.text=sharedPreferences.getString("email","")
            txtUserAddress.text=sharedPreferences.getString("address","")

        }

        return view

    }


}