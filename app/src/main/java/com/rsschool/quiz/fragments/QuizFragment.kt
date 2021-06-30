package com.rsschool.quiz.fragments

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rsschool.quiz.R
import com.rsschool.quiz.R.*
import com.rsschool.quiz.databinding.FragmentQuizBinding
import com.rsschool.quiz.interfaces.SomeInterface

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var screenNumber = 0 // номер экрана, на котором находится юзер
        val themeArr = mapOf(
            0 to style.Theme_Quiz_First,
            1 to style.Theme_Quiz_Second,
            2 to style.Theme_Quiz_Third,
            3 to style.Theme_Quiz_Fourth,
            4 to style.Theme_Quiz_Fifth
        )


        val questions = listOf(
            "Что нельзя съесть на завтрак?",
            "Сколько программистов надо, чтобы вкрутить одну лампочку?",
            "IT-шник – это ориентация или все же диагноз?",
            "Когда я думаю - это...?",
            "Смузи - это"
        )
        val userAnswers = arrayOf(0, 0, 0, 0, 0)
        val rightAnswers = listOf(1, 2, 3, 4, 5)
        val questionNames =
            listOf("Question 1", "Question 2", "Question 3", "Question 4", "Question 5")

        val statusBarColors = listOf(
            color.deep_orange_100_dark, color.yellow_100_dark, color.status_bar_3,
            color.status_bar_4, color.status_bar_5
        )

        val allAnswers = mapOf(
            0 to listOf(
                "суп",
                "тяжелую пищу",
                "обиды",
                "то, что напрограммировал вчера ночью",
                "обед && ужин"
            ),
            1 to listOf(
                "3-5",
                "6-10",
                "Ни одного. В этом случае отсутствие света – проблема на стороне железа.",
                "Ни одного. Вне компетенции",
                "Много, затрудняюсь"
            ),
            2 to listOf(
                "ориентация",
                "диагноз",
                "и то, и другое",
                "наказание",
                "Дмитрий Самущенко"
            ),
            3 to listOf(
                "мне не идет",
                "опасно",
                "опасно и мне не идет",
                "я Никита",
                "мне не идет и опасно"
            ),
            4 to listOf(
                "натовская разработка",
                "то, что похоронило IT",
                "пища богов",
                "я натурал",
                "Js"
            ),
        )


        fillOutForm(allAnswers, screenNumber) // заполняем вопросы

        binding.question.text = questions[screenNumber]
        if (screenNumber == 0) {
            binding.previousButton.isEnabled = false
        }
        binding.nextButton.isEnabled = false
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.nextButton.isEnabled = true
        }

        binding.nextButton.setOnClickListener {
            screenNumber++

            if (screenNumber > 4) {

                when (binding.radioGroup.checkedRadioButtonId) {
                    R.id.option_one -> userAnswers[screenNumber - 1] = 1
                    R.id.option_two -> userAnswers[screenNumber - 1] = 2
                    R.id.option_three -> userAnswers[screenNumber - 1] = 3
                    R.id.option_four -> userAnswers[screenNumber - 1] = 4
                    R.id.option_five -> userAnswers[screenNumber - 1] = 5
                }
                (requireActivity() as SomeInterface)
                    .startSecondFrag(
                        checkUser(userAnswers.toIntArray(), rightAnswers),
                        findStrings(userAnswers.toIntArray(), allAnswers)
                    )
            } else {
                if (screenNumber == 4) {
                    binding.nextButton.text = getString(string.submitButton)
                }

                when (binding.radioGroup.checkedRadioButtonId) {
                    R.id.option_one -> userAnswers[screenNumber - 1] = 1
                    R.id.option_two -> userAnswers[screenNumber - 1] = 2
                    R.id.option_three -> userAnswers[screenNumber - 1] = 3
                    R.id.option_four -> userAnswers[screenNumber - 1] = 4
                    R.id.option_five -> userAnswers[screenNumber - 1] = 5
                }
                fillOutForm(allAnswers, screenNumber)

                requireContext().setTheme(themeArr[screenNumber]!!)
                requireActivity().window.statusBarColor = statusBarColors[screenNumber]

                val typedValue = TypedValue()
                val currentTheme = requireContext().theme
                currentTheme?.resolveAttribute(attr.colorPrimaryVariant, typedValue, true)
                currentTheme?.resolveAttribute(attr.colorSecondaryVariant, typedValue, true)
                currentTheme?.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    currentTheme?.resolveAttribute(android.R.attr.colorSecondary, typedValue, true)
                    currentTheme?.resolveAttribute(android.R.attr.colorPrimaryDark, typedValue, true)
                }
                val window = activity?.window
                binding.toolbar.background = ColorDrawable(typedValue.data)
                binding.nextButton.background = ColorDrawable(typedValue.data)
                binding.previousButton.background = ColorDrawable(typedValue.data)
                window?.statusBarColor = typedValue.data

                binding.toolbar.title = questionNames[screenNumber]
                binding.previousButton.isEnabled = true
                binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24)

                binding.radioGroup.clearCheck()
                binding.question.text = questions[screenNumber]

                if (userAnswers[screenNumber] != 0) {
                    val previousAnswer = userAnswers[screenNumber]
                    whenWithPreviousAnswers(previousAnswer)
                } // когда nextaми возвращаемся к отвеченным вопросам

                if (userAnswers[screenNumber] == 0) {
                    it.isEnabled = false
                }
            }
        }
        binding.previousButton.setOnClickListener {
            screenNumber--

            val previousAnswer = userAnswers[screenNumber]
            if (screenNumber == 3) {
                binding.nextButton.text = getString(string.nextButton)
            }
            fillOutForm(allAnswers, screenNumber)
            if (screenNumber == 0) {
                binding.previousButton.isEnabled = false
            }
            if (screenNumber == 0) {
                binding.toolbar.navigationIcon = null
            }
            requireContext().setTheme(themeArr[screenNumber]!!)
            requireActivity().window.statusBarColor = statusBarColors[screenNumber]
            binding.toolbar.title = questionNames[screenNumber]
            binding.question.text = questions[screenNumber]
            whenWithPreviousAnswers(previousAnswer)

            val typedValue = TypedValue()
            val currentTheme = requireContext().theme
            currentTheme?.resolveAttribute(attr.colorPrimaryVariant, typedValue, true)
            currentTheme?.resolveAttribute(attr.colorSecondaryVariant, typedValue, true)
            currentTheme?.resolveAttribute(attr.colorPrimary, typedValue, true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                currentTheme?.resolveAttribute(android.R.attr.colorSecondary, typedValue, true)
                currentTheme?.resolveAttribute(android.R.attr.colorPrimaryDark, typedValue, true)
            }

            val window = activity?.window
            binding.toolbar.background = ColorDrawable(typedValue.data)
            binding.nextButton.background = ColorDrawable(typedValue.data)
            binding.previousButton.background = ColorDrawable(typedValue.data)
            window?.statusBarColor = typedValue.data
        }

        binding.toolbar.setNavigationOnClickListener {
            binding.previousButton.callOnClick()
        }
    }

    private fun whenWithPreviousAnswers(prev: Int) {
        when (prev) {
            1 -> binding.optionOne.isChecked = true
            2 -> binding.optionTwo.isChecked = true
            3 -> binding.optionThree.isChecked = true
            4 -> binding.optionFour.isChecked = true
            5 -> binding.optionFive.isChecked = true
        }
    }

    private fun fillOutForm(questions: Map<Int, List<String>>, screenNumber: Int) {
        val list = questions[screenNumber]
        binding.optionOne.text = list!![0]
        binding.optionTwo.text = list[1]
        binding.optionThree.text = list[2]
        binding.optionFour.text = list[3]
        binding.optionFive.text = list[4]
    }

    private fun checkUser(arr: IntArray, list: List<Int>): Int {
        var score = 0
        for (i in 0..4) {
            if (arr[i] == list[i])
                score++
        }
        return score * 20
    }

    private fun findStrings(
        userAnswers: IntArray,
        allAnswers: Map<Int, List<String>>
    ): List<String> {
        val retList = mutableListOf<String>()
        Log.d(
            "TAGFF",
            "userAnswers = ${userAnswers[0]} \n ${userAnswers[1]} \n${userAnswers[2]} \n ${userAnswers[3]} \n ${userAnswers[4]} \n"
        )
        for (i in 0..4) {
            val tempRes = userAnswers[i]
            Log.d("TAGFF", "i = $i, userAnswer = ${userAnswers[i]}")
            Log.d("TAGFF", "tempRes = $tempRes")
            retList.add(allAnswers[i]?.get(tempRes - 1)!!)
        }
        return retList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onBack() {

    }
}