package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rsschool.quiz.databinding.ActivityMainBinding
import com.rsschool.quiz.fragments.QuizFragment
import com.rsschool.quiz.fragments.SubmitFragment
import com.rsschool.quiz.interfaces.SomeInterface

class MainActivity : AppCompatActivity(), SomeInterface{
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initFrag()
    }

    private fun initFrag() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container,QuizFragment(),null)
            .commit()
    }

    override fun startFirstFrag() {
        initFrag()
    }

    override fun startSecondFrag(result: Int, listAnswers: List<String>) {
        openSubmitFragment(result, listAnswers)
    }

    private fun openSubmitFragment(result: Int, listAnswers: List<String>) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SubmitFragment.newInstance(result, listAnswers))
            .commit()
    }

}