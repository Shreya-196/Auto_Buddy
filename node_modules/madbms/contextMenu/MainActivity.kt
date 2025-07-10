package com.example.menu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.PopupMenu
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val B1 = findViewById<Button>(R.id.B1)
        registerForContextMenu(B1)
        B1.setOnClickListener { v -> openContextMenu(v)}
    }
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_main,menu)
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.item1 -> Toast.makeText(this,"Open Selected",Toast.LENGTH_SHORT).show()
            R.id.item2 -> Toast.makeText(this,"Search Selected",Toast.LENGTH_SHORT).show()
            R.id.item3 -> Toast.makeText(this,"Exit Selected",Toast.LENGTH_SHORT).show()
        }
        return super.onContextItemSelected(item)
    }
}