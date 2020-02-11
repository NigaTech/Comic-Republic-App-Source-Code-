package com.example.franklin.trende.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.niggatechdeveloper.comicrepublic.R


import com.niggatechdeveloper.comicrepublic.DetailsActivity
import com.niggatechdeveloper.comicrepublic.models.Trende
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(ctx: Context, mData: List<Trende>) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    val mContext = ctx
    val mData = mData
//    val option = RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape)



    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.trende_row_item, parent, false)
        val viewHolder = MyViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, positon: Int) {


        holder.trende_name.text = mData[positon].title

        if (mData[positon].img.isEmpty()){
            holder.trende_img.setImageResource(R.drawable.comiclogohome)
        }else {
            Picasso.get().load(mData[positon].img).into(holder.trende_img)

        }


        holder.itemView.setOnClickListener {


            val i = Intent(mContext,DetailsActivity::class.java)
            i.putExtra("trende_img",mData[positon].img)
            i.putExtra("trende_content",mData[positon].content)
            i.putExtra("trende_published",mData[positon].published)
            i.putExtra("trende_title", mData[positon].title)
            i.putExtra("trende_author", mData[positon].displayName)
            i.putExtra("trende_url", mData[positon].url)

            mContext.startActivity(i)

        }




//        holder.tv_rating.text = mData[positon].rating
//        holder.tv_studio.text = mData[positon].studio
//        holder.tv_category.text = mData[positon].categorie
//        holder.itemView.setOnClickListener {
//
//
//
//                val i = Intent(mContext, TrendeActivity::class.java)
//                i.putExtra("anime_name", mData[positon].name)
////                i.putExtra("anime_description", mData[positon].description)
////                i.putExtra("anime_studio", mData[positon].studio)
////                i.putExtra("anime_category", mData[positon].categorie)
////                i.putExtra("anime_nb_episode", mData[positon].nb_episode)
////                i.putExtra("anime_img", mData[positon].image_url)
////                i.putExtra("anime_rating", mData[positon].rating)
//
//
//                mContext.startActivity(i)
//
//
//        }

//        Glide.with(mContext).load(mData[positon].image_url).apply(option).into(holder.img_thumbnail)
    }


    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {


        val trende_name = itemView.findViewById<TextView>(R.id.trende_name)
        val trende_img = itemView.findViewById<ImageView>(R.id.trende_img)
        //        val tv_category = itemView.findViewById<TextView>(R.id.categorie)
//        val tv_rating = itemView.findViewById<TextView>(R.id.rating)
//        val tv_studio = itemView.findViewById<TextView>(R.id.studio)
//        val img_thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
        val container = itemView.findViewWithTag<LinearLayout>(R.id.container)

    }

}