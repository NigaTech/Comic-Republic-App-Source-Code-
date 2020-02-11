package com.niggatechdeveloper.comicrepublic.recyclerview.item

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.niggatechdeveloper.comicrepublic.Model.User
import com.niggatechdeveloper.comicrepublic.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import com.niggatechdeveloper.comicrepublic.R
import com.niggatechdeveloper.comicrepublic.util.StorageUtil
import kotlinx.android.synthetic.main.item_person.*


class PersonItem(val person: User,
                 val userId: String,
                 private val context: Context)
    : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.textView_name.text = person.name
        viewHolder.textView_bio.text = person.bio
        if (person.profilePicturePath != null)
            GlideApp.with(context)
                .load(StorageUtil.pathToReference(person.profilePicturePath))
                .placeholder(R.drawable.profile)
                .into(viewHolder.imageView_profile_picture)
    }

    override fun getLayout() = R.layout.item_person


}