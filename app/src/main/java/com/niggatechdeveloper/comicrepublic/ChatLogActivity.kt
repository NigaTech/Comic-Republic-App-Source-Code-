package com.niggatechdeveloper.comicrepublic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.textView

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)



        val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title = username

        setupDummyData()

        send_button_chat_log.setOnClickListener {
            Log.d(TAG, "Attempt to send message...")
            performSendMessage()
        }
    }

    class ChatMessage(val id: String, val text: String, val fromId: String, val toId: String, val timestamp: Long)

    private fun performSendMessage() {
        // how do we send message to firebase...
        val text = edittext_chat_log.text.toString()

        val fromId = FirebaseAuth.getInstance().uid

        val toId = FirebaseAuth.getInstance().uid

        if (fromId == null) return
        if (toId == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
        reference.setValue("chatMessage")
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
            }
    }
    private fun setupDummyData() {
        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatFromItem("From Messagefffgfgfvuj"))
        adapter.add(ChatToItem("From Message\nGNJJJfffgfgfvuj"))
        adapter.add(ChatFromItem("From Messagefffgfgfvuj"))
        adapter.add(ChatToItem("From Message\nGNJJJfffgfgfvuj"))
        adapter.add(ChatFromItem("From Messagefffgfgfvuj"))
        adapter.add(ChatToItem("From Message\nGNJJJfffgfgfvuj"))



        recyclerview_chat_log.adapter = adapter
    }
}

class ChatFromItem(val text: String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView.text = text
    }
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView.text = text
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}