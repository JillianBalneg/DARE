package com.example.dare

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyAdaptor(
    private val context: Activity,
    private val arrayList: ArrayList<absBeginnerString>
) : ArrayAdapter<absBeginnerString>(context, R.layout.list_item, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.list_item,null)

        val imageView : ImageView = view.findViewById(R.id.imgSrc)
        val name : TextView = view.findViewById(R.id.name)
        val description : TextView = view.findViewById(R.id.desc)

        imageView.setImageResource(arrayList[position].imgId)
        name.text = arrayList[position].name
        description.text = arrayList[position].desc


        return view
    }
}