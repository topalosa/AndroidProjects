package tp01.com.n8422.myquizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import tp01.com.n8422.myquizapp.databinding.ActivityQuizQuestionBinding


class QuizQuestionActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null

    private lateinit var binding: ActivityQuizQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

            when (v?.id) {

                R.id.tv_option_one -> {
                    selectedOptionView(binding.tvOptionOne, 1)
                }
                R.id.tv_option_two -> {
                    selectedOptionView(binding.tvOptionTwo, 2)
                }
                R.id.tv_option_three -> {
                    selectedOptionView(binding.tvOptionThree, 3)
                }
                R.id.tv_option_four -> {
                    selectedOptionView(binding.tvOptionFour, 4)
                }
                R.id.btn_submit -> {
                    if (mSelectedOptionPosition == 0) {
                        mCurrentPosition++

                        when{
                            mCurrentPosition <= mQuestionsList!!.size ->{
                                setQuestion()
                            }else ->{
                            val intent = Intent(this, ResultActivity::class.java)
                                intent.putExtra(Constants.USER_NAME, mUserName)
                                intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                                intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        val question = mQuestionsList?.get(mCurrentPosition - 1)
                        if (question!!.correctAnswer != mSelectedOptionPosition) {
                            answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                        }else{
                            mCorrectAnswers++
                        }
                        answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                        if (mCurrentPosition == mQuestionsList!!.size) {
                            binding.btnSubmit.text = "FINISH"
                        } else {
                            binding.btnSubmit.text = "GO TO NEXT QUESTION"
                        }
                        mSelectedOptionPosition = 0
                    }

                }
            }
    }

        private fun answerView(answer: Int, drawableView: Int) {
            when(answer){
                1 ->{
                    binding.tvOptionOne.background = ContextCompat.getDrawable(
                            this, drawableView
                    )
                }
                2 ->{
                    binding.tvOptionTwo.background = ContextCompat.getDrawable(
                            this, drawableView
                    )
                }
                3 ->{
                    binding.tvOptionThree.background = ContextCompat.getDrawable(
                            this, drawableView
                    )
                }
                4 ->{
                    binding.tvOptionFour.background = ContextCompat.getDrawable(
                            this, drawableView
                    )
                }
            }
        }


    private fun setQuestion() {

        val question = mQuestionsList!!.get(mCurrentPosition -1)

        defaultOptionsView()

        if(mCurrentPosition  == mQuestionsList!!.size){
            binding.btnSubmit.text = "FINISH"
        }else{
            binding.btnSubmit.text = "SUBMIT"
        }

        binding.progressBar.progress = mCurrentPosition
        binding.tvProgress.text = "$mCurrentPosition" + "/" + binding.progressBar.getMax()

        binding.tvQuestion.text = question!!.question
        binding.ivImage.setImageResource(question.image)
        binding.tvOptionOne.text = question.optionOne
        binding.tvOptionTwo.text = question.optionTwo
        binding.tvOptionThree.text = question.optionThree
        binding.tvOptionFour.text = question.optionFour
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {

        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(
                Color.parseColor("#363A43")
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
                this@QuizQuestionActivity,
                R.drawable.select_default_option_border_bg
        )
    }



    private fun defaultOptionsView() {

        binding.apply {

            val options = ArrayList<TextView>()
            options.add(0, tvOptionOne)
            options.add(1, tvOptionTwo)
            options.add(2, tvOptionThree)
            options.add(3, tvOptionFour)

            for (option in options) {
                option.setTextColor(Color.parseColor("#7A8089"))
                option.typeface = Typeface.DEFAULT
                option.background = ContextCompat.getDrawable(
                        this@QuizQuestionActivity,
                        R.drawable.select_default_option_border_bg
                )
            }
        }
    }
}
