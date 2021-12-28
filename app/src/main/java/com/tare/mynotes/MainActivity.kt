package com.tare.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(HomeFragment(), true)
    }

    private fun replaceFragment(fragment: Fragment, isTransition : Boolean)
    {
        val manager = supportFragmentManager.beginTransaction()
        if(isTransition)
        {
            manager.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }
        manager.replace(R.id.flHome, fragment).addToBackStack(fragment.javaClass.simpleName)
        manager.commit()
    }
}