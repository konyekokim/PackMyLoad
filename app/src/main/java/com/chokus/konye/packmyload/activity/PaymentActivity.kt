package com.chokus.konye.packmyload.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.chokus.konye.packmyload.R
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.payment_title)
        viewActions()
    }

    private fun viewActions(){
        add_card_relativeLayout.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_payment,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.edit){
            //pass intent from here to the add card Activity with details of former saved card
        }
        return super.onOptionsItemSelected(item)
    }
}
