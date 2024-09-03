package com.example.ch18_image

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ch18_image.databinding.ItemHinfoBinding

class PhpViewHolder(val binding: ItemHinfoBinding) : RecyclerView.ViewHolder(binding.root)

class PhpAdapter (val context: Context, val itemList: ArrayList<HinfoData>): RecyclerView.Adapter<PhpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhpViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PhpViewHolder(ItemHinfoBinding.inflate(layoutInflater))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: PhpViewHolder, position: Int) {
        val data = itemList.get(position)

        holder.binding.run {
            tvName.text=data.name
            tvAge.text=data.Age.toString()
            tvAddr.text=data.addr

            root.setOnClickListener{
                Toast.makeText(context,"Root",Toast.LENGTH_LONG).show()
                Intent(context,HumanActivity::class.java).apply{
                    putExtra("name",tvName.text)
                    putExtra("age",tvAge.text)
                    putExtra("addr",tvAddr.text)
                }.run {
                    context.startActivity(this)
                }
            }
            tvName.setOnClickListener {
                Toast.makeText(context,"Name",Toast.LENGTH_LONG).show()
            }
            imageView.setOnClickListener {
                Toast.makeText(context,"Image",Toast.LENGTH_LONG).show()
            }
        }
    }
}

