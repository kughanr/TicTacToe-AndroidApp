package kughanr.tictactoe

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_one_player.*
import org.jetbrains.anko.toast
import java.text.FieldPosition


class OnePlayerActivity : AppCompatActivity() {
    
    private val tag = "OnePlayerActivity"

    private var playerTurn : Boolean = true
    
    private lateinit var positions : MutableList<MutableList<Button>>
    private var fields : MutableList<MutableList<String>> = mutableListOf(mutableListOf("", "", ""), mutableListOf("", "", ""), mutableListOf("", "", ""))
    
    private var turnNumber : Int = 0
    private var roundNumber : Int = 0

    private var winCount : Int = 0
    private var drawCount: Int = 0
    private var lossCount: Int = 0
    
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_player)

        //get stats

        p1Stats.text = "Wins: $winCount Draws: $drawCount Lost: $lossCount"

        playerTurn = roundNumber % 2 == 0 //player starts even rounds

        //fill positions
        positions = mutableListOf(mutableListOf(buttonP1_00, buttonP1_01, buttonP1_02),
                mutableListOf(buttonP1_10, buttonP1_11, buttonP1_12),
                mutableListOf(buttonP1_20, buttonP1_21, buttonP1_22))

        //create onClickListeners
        positions.forEach{buttonList ->
            buttonList.forEach{button->
                button.setOnClickListener{
                    playerMove(button)
                }
            }
        }

        resetP1Button.setOnClickListener {
            finish()
            startActivity(intent)
        }
        
    }

    private fun playerMove(position: Button) {
        if (roundNumber % 2 == 0 || turnNumber != 0) {
            Log.d(tag, "Player turn $playerTurn")
            if (position.text.toString() != "") {
                toast("You can't make that move!")
                return
            }

            position.text = "X"
            makeMove(position)
        } else {
            Log.d(tag, "playerMove turn calling computer")
            checkForWin()
            computerMove()
        }
    }

    private fun makeMove(position: Button){
        turnNumber++

        if(!playerTurn){
            if(position.text == "") {
                position.text = "O"
            } else {
                toast("Error")
            }
        }
        val test = checkForWin()
        Log.d(tag, "makeMove check for win $test")
        if(checkForWin()){
            if(playerTurn){
                toast("You won!")
                winCount++
                clearBoard()
            } else {
                toast("You lost :(")
                lossCount++
                clearBoard()
            }

        } else {
            if (turnNumber == 9){
                toast("It's a draw!")
                drawCount++

                clearBoard()
            } else {
                playerTurn = !playerTurn
                Log.d(tag, "make move player turn $playerTurn")
                if(!playerTurn){
                    computerMove()
                }
            }
        }
    }

    private fun checkForWin() : Boolean {
        

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
    private fun clearBoard(){
        roundNumber++
        playerTurn = roundNumber % 2 == 0
        positions.forEach{
            it.forEach{
                it.text = ""
                }
            }
        p1Stats.text = "Wins: $winCount Draws: $drawCount Lost: $lossCount"
        turnNumber = 0
        if(!playerTurn){
            computerMove()
        }
    }
    
    private fun computerMove() {
        //if middle square empty, play middle square (only will occur on the first turn, no chance of losing)
        if (buttonP1_11.text == ""){
            makeMove(buttonP1_11)
            return
        } else {
            toast("WOOF")
        }

        //plays for instant win or prevent instant lost

        //horizontal
        for(i in 0..1){
            for (j in 0..1){
                if (fields[i][j] == fields[i][j+1] && fields[i][j]!="" && fields[i][(j+2)%3]==""){
                    makeMove(getButton((i) % 3,(j+2)%3 ))
                    return
                }
            }
        }
        //vertical
        for(i in 0..1){
            for (j in 0..1){
                if (fields[i][j] == fields[i+1][j] && fields[i][j] != "" && fields[(i+2)%3][j]==""){
                    Log.d(tag, "Button ${fields[i][j]}, ${fields[i+1][j]}")
                    makeMove(getButton((i+2)%3 ,(j) % 3))
                    return
                }
            }
        }
        //diagonal top-left to bottom-right
        if (fields[0][0] == fields[1][1] && fields[1][1] != "" && fields[2][2] == ""){
            makeMove(getButton(2,2))
            return
        }
        if (fields[1][1] == fields[2][2] && fields[1][1] != "" && fields[0][0] == ""){
            makeMove(getButton(0,0))
            return
        }
        //diagonal top-right to bottom-left
        if (fields[2][0] == fields[1][1] && fields[1][1] != "" && fields[0][2] == ""){
            makeMove(getButton(0,2))
            return
        }
        if (fields[1][1] == fields[0][2] && fields[1][1] != "" && fields[2][0] == ""){
            makeMove(getButton(2,0))
            return
        }

        //play to improve winning chances
        for (i in 0..1){
            for (j in 0..1){
                if (fields[i][j] == "O"){
                    if(fields[i+1][j] == ""){
                        makeMove(getButton(i+1, j))
                        return
                    }
                    if(fields[i][j+1] == ""){
                        makeMove(getButton(i, j+1))
                        return
                    }

                }
            }
        }

        //ensure move is made

        for(i in 0..2){
            for(j in 0..2){
                if (fields[i][j] == ""){
                    makeMove(getButton(i,j))
                    return
                }
            }
        }

    }

    private fun getButton(i : Int, j: Int): Button {
        val buttonID = "buttonP1_$i$j"
        Log.d(tag, "Button $buttonID")
        val resID = resources.getIdentifier(buttonID, "id", packageName)
        return findViewById(resID)
    }
}
