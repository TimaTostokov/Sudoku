package com.example.my.sudoku.view

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.my.sudoku.R
import com.example.my.sudoku.game.Cell
import com.example.my.sudoku.view.custom.SudokuBoardView
import com.example.my.sudoku.viewmodel.PlaySudokuViewModel

class MainActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {

    private lateinit var viewModel: PlaySudokuViewModel
    private lateinit var numberButtons: List<Button>
    private lateinit var sudokuBoardView: SudokuBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sudokuBoardView = findViewById(R.id.sudokuBoardView)

        viewModel = ViewModelProvider(this).get(PlaySudokuViewModel::class.java)

        viewModel.sudokuGame.selectedCellLiveData.observe(
            this,
            Observer {
                updateSelectedCellUI(it)
            })
        viewModel.sudokuGame.cellsLiveData.observe(this, Observer {
            updateCells(it)
        })
        viewModel.sudokuGame.isTakingNotesLiveData.observe(
            this,
            Observer {
                updateNoteTakingUI(it)
            })
        viewModel.sudokuGame.highlightedKeysLiveData.observe(
            this,
            Observer { updateHighlightedKeys(it) })

        numberButtons = listOf(
            findViewById(R.id.oneButton), findViewById(R.id.twoButton),
            findViewById(R.id.threeButton), findViewById(R.id.fourButton),
            findViewById(R.id.fiveButton), findViewById(R.id.sixButton),
            findViewById(R.id.sevenButton), findViewById(R.id.eightButton),
            findViewById(R.id.nineButton)
        )

        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener { viewModel.sudokuGame.handleInput(index + 1) }
        }

        findViewById<Button>(R.id.notesButton).setOnClickListener { viewModel.sudokuGame.changeNoteTakingState() }
        findViewById<Button>(R.id.deleteButton).setOnClickListener { viewModel.sudokuGame.delete() }
    }

    private fun updateCells(cells: List<Cell>?) {
        cells?.let {
            sudokuBoardView.updateCells(it)
        }
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) {
        cell?.let { (row, col) ->
            sudokuBoardView.updateSelectedCellUI(row, col)
        }
    }

    private fun updateNoteTakingUI(isNoteTaking: Boolean?) {
        isNoteTaking?.let {
            val color = if (it) ContextCompat.getColor(this, R.color.colorPrimary) else Color.LTGRAY
            findViewById<Button>(R.id.notesButton).background.setColorFilter(
                color,
                PorterDuff.Mode.MULTIPLY
            )
        }
    }

    private fun updateHighlightedKeys(set: Set<Int>?) {
        set?.let {
            numberButtons.forEachIndexed { index, button ->
                val color = if (set.contains(index + 1)) ContextCompat.getColor(
                    this,
                    R.color.colorPrimary
                ) else Color.LTGRAY
                button.background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
            }
        }
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }

}
