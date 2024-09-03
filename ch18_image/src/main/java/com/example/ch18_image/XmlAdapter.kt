package com.example.ch18_image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ch18_image.databinding.ItemMainBinding

class XmlViewHolder(val binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
class XmlAdapter(val datas:MutableList<myXmlItem>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {     // 1-1
    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return XmlViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as XmlViewHolder).binding
        val model = datas!![position]           // 1-2

        // 1-3
        binding.kindCd.text = "품종: ${model.kindCd}"
        binding.happenDt.text = model.happenDt
        binding.careAddr.text = model.careAddr
        binding.processState.text = model.processState
        binding.sexCd.text= "성별: ${model.sexCd}"
        binding.careTel.text= "전화번호: ${model.careTel}"

        Glide.with(binding.root)
            .load(model.popfile)
            .override(400,300)
            .into(binding.popfile)


    }
}