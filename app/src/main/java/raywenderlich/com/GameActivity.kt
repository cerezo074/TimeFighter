package raywenderlich.com

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.IntegerRes
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class GameActivity: AppCompatActivity() {

    internal val TAG = GameActivity::class.java.simpleName
    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal lateinit var tapMeButton: Button
    internal var score = 0

    internal var gameStared = false
    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown = 10
    internal var countDownInterval: Long = 1000
    internal var timeLeft = initialCountDown

    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    //LifeCycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called. Score is: $score")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameScoreTextView = findViewById<TextView>(R.id.game_score_text_view)
        timeLeftTextView = findViewById<TextView>(R.id.time_left_text_view)
        tapMeButton = findViewById<Button>(R.id.tap_me_button)

        tapMeButton.setOnClickListener { v -> incrementScore() }

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()

            if (timeLeft != initialCountDown) {
                startGame()
            }
        } else {
            resetGame()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(SCORE_KEY, score)
        outState?.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState: Saving score: $score & Time Left: $timeLeft")
    }

    override fun onStop() {
        super.onStop()

        countDownTimer.cancel()
        gameStared = false
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()

        if (timeLeft != initialCountDown) {
            startGame()
        }
    }

    //Game Methods
    private fun incrementScore() {
        if (!gameStared) {
            startGame()
        }

        score++
        gameScoreTextView.text = getString(R.string.your_score, Integer.toString(score))
    }

    private fun resetGame() {
        score = 0
        timeLeft = initialCountDown

        val initialScore = getString(R.string.your_score, Integer.toString(score))
        gameScoreTextView.text = initialScore

        val initialTimeLeft = getString(R.string.time_left, Integer.toString(initialCountDown))
        timeLeftTextView.text = initialTimeLeft
        setTimer(initialTime = initialCountDown)

        gameStared = false
    }

    private fun restoreGame() {
        val restoredScore = getString(R.string.your_score, Integer.toString(score))
        gameScoreTextView.text = restoredScore

        val restoreTime = getString(R.string.time_left, Integer.toString(timeLeft))
        timeLeftTextView.text = restoreTime
    }

    private fun startGame() {
        setTimer(initialTime = timeLeft)
        countDownTimer.start()
        gameStared = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_over_message, Integer.toString(score)), Toast.LENGTH_LONG).show()
        resetGame()
    }

    //Timer Methods
    private fun setTimer(initialTime: Int) {
        countDownTimer = object : CountDownTimer((initialTime * 1000).toLong(), countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left, Integer.toString(timeLeft))
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }

        }
    }

}
