package com.codewithritom.rg_assistant.ui.assistant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.codewithritom.rg_assistant.databinding.FragmentAssistantBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class AssistantFragment : Fragment() {

    private var _binding: FragmentAssistantBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val username:String = "usr"
    private var textToSpeech:TextToSpeech? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAssistantBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val sendButton:Button = binding.sendbutton
        val messageBox:EditText = binding.messageBox
        val mic:ImageView = binding.assistantmic
        textToSpeech = TextToSpeech(
            context
        ) { i ->
            // if No error is found then only it will run
            if (i != TextToSpeech.ERROR) {
                // To Choose language of speech
                textToSpeech!!.language = Locale.UK
            }
            else Log.e("texttospeech","Text To Speech failed")
        }
        sendButton.setOnClickListener {
            speakAndAnswer(messageBox.text.toString())
        }

        mic.setOnClickListener{
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
            val result = intent.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            )
            (result)?.get(0)?.let { speakAndAnswer(it) }
//            try {
//                startActivityForResult(intent, 1)
//            } catch (e: Exception) {
//                Toast
//                    .makeText(
//                        context, " " + e.message,
//                        Toast.LENGTH_SHORT
//                    )
//                    .show()
//            }
        }

        return root
    }
    private var answerLine = "couldn't get answer try again"
    fun getAnswerFromBrainShop(question:String):String {
        var line:String? = null;
        val thread: Thread = object : Thread() {
            override fun run() {
                val url = URL("http://api.brainshop.ai/get?bid=167322&key=U1RhVdvcXF9BNLZL&uid=$username&msg=$question")
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection;
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                while (reader.readLine().also { line = it } != null) {
                    answerLine = line!!.trim { it <= ' ' }
                    answerLine = answerLine.replace("{\"cnt\":\"", "").replace("\"}","")
                }
                reader.close()
            }
        }
        thread.start()
        return answerLine
    }

    fun speakAndAnswer(question: String) {
        binding.answer.setText(getAnswerFromBrainShop(question))
        textToSpeech!!.speak(getAnswerFromBrainShop(question).replace("RitomG","Ritam G"),TextToSpeech.QUEUE_FLUSH,null)
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                )
                (result)?.get(0)?.let { speakAndAnswer(it) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}