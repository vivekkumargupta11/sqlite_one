package com.example.sqlite_one

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import androidx.recyclerview.widget.RecyclerView


class DataAdapter(
    private var dataList: List<DataItem>,
    private val onDeleteClick: (Int) -> Unit,
    private val onUpdateClick: (DataItem) -> Unit,
    private val context: Context

) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataItem: DataItem) {
            val textName = itemView.findViewById<TextView>(R.id.nameTextView)
            val textDescription = itemView.findViewById<TextView>(R.id.descriptionTextView)
            val btnDelete = itemView.findViewById<TextView>(R.id.btnDelete)
            val btnUpdate = itemView.findViewById<TextView>(R.id.btnUpdate)

            textName.text = dataItem.name
            textDescription.text = dataItem.description

            btnDelete.setOnClickListener {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Delete Item!")
                alertDialog.setMessage("Are you sure to delete this item!")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    onDeleteClick(dataItem.id)
                }
                alertDialog.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                val Dialog = alertDialog.create()
                Dialog.show()
            }

            btnUpdate.setOnClickListener {
                onUpdateClick(dataItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun updateData(newData: List<DataItem>) {
        dataList = newData
        notifyDataSetChanged()
    }
}