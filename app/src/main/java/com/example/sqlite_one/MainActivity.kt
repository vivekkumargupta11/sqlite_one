package com.example.sqlite_one

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var dataAdapter: DataAdapter
    private lateinit var recyclerView:RecyclerView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addButton = findViewById<Button>(R.id.addButton)
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val descriptionEditText = findViewById<EditText>(R.id.emailEditText)
        recyclerView = findViewById(R.id.recyclerView)

        databaseHelper = DatabaseHelper(this)
        dataAdapter = DataAdapter(databaseHelper.getAllData(), this::onDeleteClick, this::onUpdateClick, this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = dataAdapter // Set the adapter here
        }

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty()) {
                val id = databaseHelper.insertData(name, description)

                if (id != -1L) {
                    dataAdapter.updateData(databaseHelper.getAllData())
                    dataAdapter.notifyDataSetChanged()
                    Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to add data", Toast.LENGTH_SHORT).show()
                }

                nameEditText.text.clear()
                descriptionEditText.text.clear()
            } else {
                Toast.makeText(this, "Please enter both name and description", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun onDeleteClick(id: Int) {
        val deletedRows = databaseHelper.deleteData(id)
        if (deletedRows > 0) {
            dataAdapter = DataAdapter(databaseHelper.getAllData(), this::onDeleteClick, this::onUpdateClick, this)
            recyclerView.adapter = dataAdapter
        }
    }
    private fun onUpdateClick(dataItem: DataItem) {
        showUpdateDialog(dataItem)
    }

    @SuppressLint("MissingInflatedId")
    private fun showUpdateDialog(dataItem: DataItem) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.updated_dialog, null)

        val nameEditText = dialogView.findViewById<EditText>(R.id.updateNameEditText)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.updateDescriptionEditText)

        nameEditText.setText(dataItem.name)
        descriptionEditText.setText(dataItem.description)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Update Data")
            .setPositiveButton("Update") { dialog, _ ->
                val newName = nameEditText.text.toString()
                val newDescription = descriptionEditText.text.toString()

                val updatedRows = databaseHelper.updateData(dataItem.id, newName, newDescription)

                if (updatedRows > 0) {
                    dataAdapter = DataAdapter(databaseHelper.getAllData(), this::onDeleteClick, this::onUpdateClick,this)
                    recyclerView.adapter = dataAdapter
                    Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
        }

}