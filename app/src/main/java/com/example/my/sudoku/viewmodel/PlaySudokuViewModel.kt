package com.example.my.sudoku.viewmodel

import androidx.lifecycle.ViewModel
import com.example.my.sudoku.game.SudokuGame

class PlaySudokuViewModel: ViewModel() {

    val sudokuGame = SudokuGame()
}