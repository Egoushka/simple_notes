package com.languagexx.simplenotes.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.languagexx.simplenotes.R
import com.languagexx.simplenotes.persistence.Note
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : DaggerAppCompatActivity() {

    private lateinit var navController: NavController
    private  lateinit var fragment: Fragment
    companion object {
        @JvmStatic private var curTheme = -1
    }
    // Method #1
    override fun onCreate(savedInstanceState: Bundle?) {

        curTheme = if(curTheme == -1){
            theme.applyStyle(R.style.AppTheme, true)
            1
        } else{
            theme.applyStyle(R.style.DarkAppTheme, true)
            -1
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.container)

        //Floating action button will redirect to Add New Notes Fragment #AddFragment
        add_new_note.setOnClickListener {
            onFloatingClicked()
        }
        change_theme.setOnClickListener {
            val intent = intent // from getIntent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            startActivity(intent)
        }
    }

    // Method #2
    private fun onFloatingClicked() {
        navController.navigate(R.id.action_listFragment_to_addFragment)
        add_new_note.hide()
    }

    // Method #3
    fun showFloatingButton() {
        add_new_note.show()
        add_new_note.visibility = View.VISIBLE
    }

}