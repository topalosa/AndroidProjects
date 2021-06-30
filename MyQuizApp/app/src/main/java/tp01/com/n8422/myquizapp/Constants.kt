package tp01.com.n8422.myquizapp


object Constants {

    const val USER_NAME: String = "user_name"
    const val TOTAL_QUESTIONS: String = "total_questions"
    const val CORRECT_ANSWERS: String = "correct_answers"

    fun getQuestions(): ArrayList<Question>{
        val questionsList = ArrayList<Question>()

        val que1 = Question(
                1,
                "What country does this flag belong to?",
                R.drawable.ic_flag_of_argentina,
                "Argentina",
                "Australia",
                "Armenia",
                "Austria",
                1
        )
        questionsList.add(que1)

        val que2 = Question(
                2,
                "What country does this flag belong to?",
                R.drawable.ic_flag_of_brazil,
                "Finland",
                "Brazil",
                "Belgium",
                "Senegal",
                2
        )
        questionsList.add(que2)

        val que3 = Question(
                3,
                "What country does this flag belong to?",
                R.drawable.ic_flag_of_belgium,
                "Sweden",
                "Iceland",
                "Belgium",
                "Austria",
                3
        )
        questionsList.add(que3)

        val que4 = Question(
                4,
                "What country does this flag belong to?",
                R.drawable.ic_flag_of_kuwait,
                "Iraq",
                "Australia",
                "Japan",
                "Kuwait",
                4
        )
        questionsList.add(que4)

        val que5 = Question(
                5,
                "What country does this flag belong to?",
                R.drawable.ic_flag_of_germany,
                "India",
                "Germany",
                "China",
                "France",
                2
        )
        questionsList.add(que5)

        val que6 = Question(
                6,
                "What country does this flag belong to?",
                R.drawable.ic_flag_of_denmark,
                "Denmark",
                "Norway",
                "Albania",
                "Spain",
                1
        )
        questionsList.add(que6)

        val que7 = Question(
                1,
                "What country does this flag belong to?",
                R.drawable.ic_flag_of_fiji,
                "Indonesia",
                "Australia",
                "Venezuela",
                "Fiji",
                4
        )
        questionsList.add(que7)

        val que8 = Question(
                1,
                "What country does this flag belong to?",
                R.drawable.ic_flag_of_new_zealand,
                "Vietnam",
                "Belgium",
                "New Zealand",
                "South Africa",
                3
        )
        questionsList.add(que8)

        val que9 = Question(
                1,
                "What country does this flag belong to?",
                R.drawable.ic_flag_of_india,
                "India",
                "Uruguay",
                "Bulgaria",
                "Finland",
                1
        )
        questionsList.add(que9)

        val que10 = Question(
                1,
                "What country does this flag belong to?",
                R.drawable.ic_flag_of_australia,
                "Egypt",
                "Australia",
                "Sweden",
                "Russia",
                2
        )
        questionsList.add(que10)

        val que11 = Question(
                1,
                "What country does this flag belong to?",
                R.drawable.ic_flag_of_sweden,
                "Sweden",
                "Russia",
                "Finland",
                "France",
                1
        )
        questionsList.add(que11)

        val que12 = Question(
            1,
            "What country does this flag belong to?",
            R.drawable.ic_flag_of_russia,
            "China",
            "Russia",
            "Denmark",
            "Poland",
            2
        )
        questionsList.add(que12)





        return questionsList

    }
}