package kughanr.tictactoe

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_two_player.*
import org.jetbrains.anko.toast

class TwoPlayerActivity : AppCompatActivity() {

    private lateinit var positions : MutableList<MutableList<Button>>

    private var player1Turn : Boolean = true

    private var roundNumber : Int = 0

    private var p1Points : Double = 0.0
    private var p2Points : Double = 0.0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two_player)

        p1Score.text = "Player 1: $p1Points"
        p2Score.text = "Player 2: $p2Points"

        //fill positions
        positions = mutableListOf(mutableListOf(button_00, button_01, button_02), mutableListOf(button_10, button_11, button_12), mutableListOf(button_20, button_21, button_22))

        //create onClickListeners
        positions.forEach{buttonList ->
            buttonList.forEach{button->
                button.setOnClickListener{
                    makeMove(button)
                }
            }
        }

        resetButton.setOnClickListener {
            p1Points = 0.0
            p2Points = 0.0
            clearBoard()
        }

    }

    private fun makeMove(position: Button){
        if(position.text.toString() != ""){
            toast("You can't make that move")
            return
        }

        if (player1Turn) {
            position.text = "X"
        } else {
            position.text = "O"
        }

        roundNumber++


            if(checkForWin()){
                if (player1Turn){
                    //player 1 wins
                    p1Points++
                    clearBoard()
                    toast("Player 1 Wins!")
                } else {
                    //player 2 wins
                    p2Points++
                    clearBoard()
                    toast("Player 2 Wins!")
                }
            } else {
                if (roundNumber == 9){
                    //draw
                    p1Points+=0.5
                    p2Points+=0.5
                    clearBoard()
                    toast("It was a draw!")
                } else {
                    player1Turn = !player1Turn //swaps turn
                }
            }


    }

    fun checkForWin() : Boolean {
        var fields : MutableList<MutableList<String>> = mutableListOf(mutableListOf("", "", ""), mutableListOf("", "", ""), mutableListOf("", "", ""))

        //fill position strings with X's and O's
        for (i in 0..2){
            for (j in 0..2){
                fields[i][j] = positions[i][j].text.toString()
            }
        }

        //win conditions

        //horizontal line
        for (i in 0..2){
            if (fields[i][0] != "" && fields[i][0] == fields[i][1] && fields[i][0] == fields[i][2]){
                return true
            }
        }

        //vertical line
        for (i in 0..2){
            if (fields[0][i] != "" && fields[0][i] == fields[1][i] && fields[0][i] == fields[2][i]){
                return true
            }
        }

        //diagonals
        if (fields[1][1] != ""){
            if (fields[0][0] == fields[1][1] && fields[0][0] == fields[2][2]){
                return true
            }
            if (fields[0][2] == fields[1][1] && fields[0][2] == fields[2][0]){
                return true
            }
        }

        //if no win conditions met
        return false
    }

    @SuppressLint("SetTextI18n")
    fun clearBoard(){
        positions.forEach{
            it.forEach{
                it.text = ""
            }
        }
        p1Score.text = "Player 1: $p1Points"
        p2Score.text = "Player 2: $p2Points"
        roundNumber = 0
    }



}
