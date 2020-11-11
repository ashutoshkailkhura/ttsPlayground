package com.example.boloapp

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var mText: String
    private val isSpeaking = false
    private val finalText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
//        val text = findViewById<TextView>(R.id.dummy_text)
        val hiText = findViewById<TextView>(R.id.dummy_text_hindi)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            hiText.setText(
                Html.fromHtml(
                    "<p>A TextToSpeech instance can only be used to synthesize text once it has completed its\n" +
                            " initialization. Implement the <code translate=\"no\" dir=\"ltr\"><a href=\"/reference/android/speech/tts/TextToSpeech.OnInitListener\">TextToSpeech.OnInitListener</a></code> to be\n" +
                            " notified of the completion of the initialization.<br>\n" +
                            " When you are done using the TextToSpeech instance, call the <code translate=\"no\" dir=\"ltr\"><a href=\"/reference/android/speech/tts/TextToSpeech#shutdown()\">shutdown()</a></code> method\n" +
                            " to release the native resources used by the TextToSpeech engine.\n" +
                            "\n<img src=\"https://www.w3schools.com/html/pic_trulli.jpg\" alt=\"Trulli\" width=\"500\" height=\"333\">" +
                            " Apps targeting Android 11 that use text-to-speech should declare <code translate=\"no\" dir=\"ltr\"><a href=\"/reference/android/speech/tts/TextToSpeech.Engine#INTENT_ACTION_TTS_SERVICE\">TextToSpeech.Engine#INTENT_ACTION_TTS_SERVICE</a></code> in the <code translate=\"no\" dir=\"ltr\">queries</code> elements of their\n" +
                            " manifest:\n" +
                            "\n" +
                            " </p>"
                )
            )
        } else {
            hiText.setText(
                Html.fromHtml(
                    "<p>A TextToSpeech instance can only be used to synthesize text once it has completed its\n" +
                            " initialization. Implement the <code translate=\"no\" dir=\"ltr\"><a href=\"/reference/android/speech/tts/TextToSpeech.OnInitListener\">TextToSpeech.OnInitListener</a></code> to be\n" +
                            " notified of the completion of the initialization.<br>\n" +
                            " When you are done using the TextToSpeech instance, call the <code translate=\"no\" dir=\"ltr\"><a href=\"/reference/android/speech/tts/TextToSpeech#shutdown()\">shutdown()</a></code> method\n" +
                            " to release the native resources used by the TextToSpeech engine.\n" +
                            "\n<img src=\"https://www.w3schools.com/html/pic_trulli.jpg\" alt=\"Trulli\" width=\"500\" height=\"333\">" +
                            " Apps targeting Android 11 that use text-to-speech should declare <code translate=\"no\" dir=\"ltr\"><a href=\"/reference/android/speech/tts/TextToSpeech.Engine#INTENT_ACTION_TTS_SERVICE\">TextToSpeech.Engine#INTENT_ACTION_TTS_SERVICE</a></code> in the <code translate=\"no\" dir=\"ltr\">queries</code> elements of their\n" +
                            " manifest:\n" +
                            "\n" +
                            " </p>"
                )
            );
        }
        hiText.movementMethod = object : UrlSpanLinkMovementMethod() {
            override fun onLinkClicked(url: String?) {
                Log.d("TAG", "link : $url");
                Toast.makeText(this@MainActivity, url, Toast.LENGTH_SHORT).show()
            }

        }
        mText = hiText.text.toString()
        button.setOnClickListener {
            ConvertTextToSpeech(mText)
        }




        tts = TextToSpeech(this, this)

        tts.setPitch(2f);
        tts.setSpeechRate(2f);

        val speechListener = object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                TODO("Not yet implemented")
            }

            override fun onDone(utteranceId: String?) {
                TODO("Not yet implemented")
            }

            override fun onError(utteranceId: String?) {
                TODO("Not yet implemented")
            }

        }
        tts.setOnUtteranceProgressListener(speechListener)
    }

    override fun onStart() {
        super.onStart()
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val textToSay = "Hello world, this is a test message!"
                val map = HashMap<String, String>()
                map[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "UniqueID"
                tts.speak(textToSay, TextToSpeech.QUEUE_ADD, map)
            }
        }
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

    private fun ConvertTextToSpeech(text: String) {

        if ("" == text) {
            val ntext = "Content not available"
            val map = HashMap<String, String>()
            map[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "UniqueID"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            }
        } else {
            val map = HashMap<String, String>()
            map[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "UniqueID"
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, map)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val locales = Locale.getAvailableLocales()
            val localeList: MutableList<Locale> = ArrayList()
            for (locale in locales) {
                val res = tts.isLanguageAvailable(locale)
                if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    Log.d("TAG", "locale : ${locale.toString()}");
                    localeList.add(locale)
                }
            }
            Log.d("TAG", "locales : ${locales.toString()}");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tts.language = resources.configuration.locales.get(0)
            } else {
                //noinspection deprecation
                tts.language = this.resources.configuration.locale
            }
            val result = tts.setLanguage(Locale.US)

            Log.d("TAG", "result : $result");
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {

                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "This Language is not supported");
            } else {
                ConvertTextToSpeech(mText)
            }
        } else
            Log.e("TAG", "Initilization Failed!");
    }


}
