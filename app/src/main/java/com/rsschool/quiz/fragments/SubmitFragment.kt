package com.rsschool.quiz.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentResultBinding
import com.rsschool.quiz.interfaces.SomeInterface
import kotlin.system.exitProcess

class SubmitFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = arguments?.getInt(RESULT_TEST)
        val list = arguments?.getStringArrayList(RESULT_TEST)
        val temp = "Ваш результат - $result%"

        binding.tvResult.text = temp
        binding.shareButton.setOnClickListener {
            val textMessage = temp
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textMessage)
                type = "text/plain"
            }
            try {
                startActivity(sendIntent)
            } catch (e: ActivityNotFoundException) {
                e.stackTrace
            }
        }
        binding.backButtonResult.setOnClickListener {
            (requireActivity() as SomeInterface).startFirstFrag()
        }
        binding.exitButton.setOnClickListener {
            exitProcess(0)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(res: Int, list: List<String>): SubmitFragment {
            val fragment = SubmitFragment()
            val args = bundleOf(RESULT_TEST to res, ANSWERS_LIST to list)
            fragment.arguments = args
            return fragment
        }

        private const val RESULT_TEST = "RESULT_TEST"
        private const val ANSWERS_LIST = "ANSWERS_LIST"
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}