package raywenderlich.com

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.IntegerRes
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class GameActivity: AppCompatActivity() {

    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal lateinit var tapMeButton: Button
    internal var score = 0

    internal var gameStared = false
    internal lateinit var countDownTimer: CountDownTimer
    internal var initialCountDown: Long = 60000
    internal var countDownInterval: Long = 1000
    internal var timeLeft = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameScoreTextView = findViewById<TextView>(R.id.game_score_text_view)
        timeLeftTextView = findViewById<TextView>(R.id.time_left_text_view)
        tapMeButton = findViewById<Button>(R.id.tap_me_button)

        tapMeButton.setOnClickListener { v -> incrementScore() }
        resetGame()
    }

    private fun incrementScore() {
        if (!gameStared) {
            startGame()
        }

        score++
        gameScoreTextView.text = getString(R.string.your_score, Integer.toString(score))
    }

    private fun resetGame() {
        score = 0

        val initialScore = getString(R.string.your_score, Integer.toString(score))
        gameScoreTextView.text = initialScore

        val initialTimeLeft = getString(R.string.time_left, Integer.toString(60))
        timeLeftTextView.text = initialTimeLeft

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left, Integer.toString(timeLeft))
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }

        }

        gameStared = false
    }

    private fun startGame() {
        countDownTimer.start()
        gameStared = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_over_message, Integer.toString(score)), Toast.LENGTH_LONG).show()
        resetGame()
    }

}
