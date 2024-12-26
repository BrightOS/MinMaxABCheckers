import de.vandermeer.asciitable.AT_Context
import de.vandermeer.asciitable.AsciiTable

class State(var Board: Array<Array<String>>, var p: Player) {
    var number: Array<Char> = arrayOf('1', '2', '3', '4', '5', '6')
    var letter: Array<Char> = arrayOf('A', 'B', 'C', 'D', 'E', 'F')
    var hRows: HashMap<Char, Int> = HashMap()
    var hCols: HashMap<Char, Int> = HashMap()

    fun start() {
        for (i in 0..5) {
            for (j in 0..5) {
                if (i % 2 != j % 2) {
                    if (i < 2) {
                        Board[i][j] = b
                    } else if (i >= 4) {
                        Board[i][j] = w
                    } else {
                        Board[i][j] = " "
                    }
                } else if (Board[i][j] == null) {
                    Board[i][j] = " "
                }
            }
        }
    }

    fun cloneBoard(b: Array<Array<String>>): Array<Array<String>> {
        val nb = Array(6) {
            Array(6) { " " }
        }
        for (i in 0..5) {
            for (j in 0..5) {
                nb[i][j] = b[i][j]
            }
        }
        return nb
    }

    fun NextMoves(p: Player?, s: State): ArrayList<Move> {
        val moves = ArrayList<Move>()
        val newBoard = cloneBoard(s.Board)
        for (i in 0..5) {
            for (j in 0..5) {
                if (isJumpPossible(Move(p!!, i, j, i + 2, j + 2), newBoard)) {
                    moves.add(Move(p, i, j, i + 2, j + 2))
                }
                if (isJumpPossible(Move(p, i, j, i - 2, j + 2), newBoard)) {
                    moves.add(Move(p, i, j, i - 2, j + 2))
                }
                if (isJumpPossible(Move(p, i, j, i + 2, j - 2), newBoard)) {
                    moves.add(Move(p, i, j, i + 2, j - 2))
                }
                if (isJumpPossible(Move(p, i, j, i - 2, j - 2), newBoard)) {
                    moves.add(Move(p, i, j, i - 2, j - 2))
                }
            }
        }
        if (moves.isNotEmpty()) return moves

        for (i in 0..5) {
            for (j in 0..5) {
                if (isMovePossible(Move(p!!, i, j, i + 1, j + 1), newBoard)) {
                    moves.add(Move(p, i, j, i + 1, j + 1))
                }
                if (isMovePossible(Move(p, i, j, i - 1, j + 1), newBoard)) {
                    moves.add(Move(p, i, j, i - 1, j + 1))
                }
                if (isMovePossible(Move(p, i, j, i + 1, j - 1), newBoard)) {
                    moves.add(Move(p, i, j, i + 1, j - 1))
                }
                if (isMovePossible(Move(p, i, j, i - 1, j - 1), newBoard)) {
                    moves.add(Move(p, i, j, i - 1, j - 1))
                }
            }
        }
        return moves
    }

    fun NextStates(moves: ArrayList<Move>, s: State): ArrayList<State> {
        val states = ArrayList<State>()
        for (a in moves) {
            val tempBoard = cloneBoard(s.Board)
            val tempState = State(tempBoard, p)
            states.add(applyMove(a, tempState))
        }
        return states
    }

    fun applyMove(a: Move, s: State): State {
        val p = a.player
        val fromRow = a.inRow
        val fromCol = a.inCol
        val toRow = a.fiRow
        val toCol = a.fiCol
        val board = s.Board

        if (isMovePossible(a, board)) {
            board[a.fiRow][a.fiCol] = board[a.inRow][a.inCol]
            board[a.inRow][a.inCol] = " "
        }

        if (isJumpPossible(a, board)) {
            board[toRow][toCol] = board[fromRow][fromCol]
            board[fromRow][fromCol] = " "
            val jumpRow = (fromRow + toRow) / 2
            val jumpCol = (fromCol + toCol) / 2
            board[jumpRow][jumpCol] = " "
        }
        if (toRow == 0 && board[toRow][toCol] === w) {
            board[toRow][toCol] = wK
        }
        if (toRow == 5 && board[toRow][toCol] === b) {
            board[toRow][toCol] = bK
        }
        return State(board, p)
    }

    fun updateBoard(s: State) {
        for (i in Board.indices) {
            for (j in Board.indices) {
                Board[i][j] = s.Board[i][j]
            }
        }
    }

    fun fillMap() {
        for (i in letter.indices) {
            hRows[letter[i]] = i
        }
        for (i in number.indices) {
            hCols[number[i]] = i
        }
    }

    fun getInput(p: Player?, input: String): Move {
        val temp = input.toCharArray()
        val values = IntArray(temp.size)
        var fromRow = 0
        var fromCol = 0
        var toRow = 0
        var toCol = 0
        for (i in temp.indices) {
            if (hRows.containsKey(temp[i])) {
                values[i] = hRows[temp[i]]!!
            } else if (hCols.containsKey(temp[i])) {
                values[i] = hCols[temp[i]]!!
            }
        }
        fromRow = values[0]
        fromCol = values[1]
        toRow = values[3]
        toCol = values[4]

        return Move(p!!, fromRow, fromCol, toRow, toCol)
    }

    fun isValidInput(p: Player?, input: String): Boolean {
        if (input.length != 5) {
            return false
        }
        val temp = input.toCharArray()

        val values = IntArray(temp.size)
        var fromRow = 0
        var fromCol = 0
        var toRow = 0
        var toCol = 0
        for (i in temp.indices) {
            if (hRows.containsKey(temp[i])) {
                values[i] = hRows[temp[i].uppercaseChar()]!!
            } else if (hCols.containsKey(temp[i])) {
                values[i] = hCols[temp[i].uppercaseChar()]!!
            }
        }
//        println("Values: ${values.joinToString()}")
        fromRow = values[0]
        fromCol = values[1]
        toRow = values[3]
        toCol = values[4]
        if (!isMovePossible(Move(p!!, fromRow, fromCol, toRow, toCol), this.Board) && !isJumpPossible(
                Move(
                    p, fromRow, fromCol, toRow, toCol
                ), this.Board
            )
        ) {
            println("Неправильный ввод, попробуйте снова: ")
            return false
        }
        return true
    }

//    fun newStateIfCanEatSomeone(p: Player, s: State): State? {
//        val newBoard = cloneBoard(s.Board)
//        repeat(6) { i ->
//            repeat(6) { j ->
//                when
//            }
//        }
//        return s
//    }

    fun AnyMovePossible(p: Player?, s: State): Boolean {
        val newBoard = cloneBoard(s.Board)
        for (i in 0..5) {
            for (j in 0..5) {
                if (isJumpPossible(Move(p!!, i, j, i + 2, j + 2), newBoard)) {
                    return true
                }
                if (isJumpPossible(Move(p, i, j, i - 2, j + 2), newBoard)) {
                    return true
                }
                if (isJumpPossible(Move(p, i, j, i + 2, j - 2), newBoard)) {
                    return true
                }
                if (isJumpPossible(Move(p, i, j, i - 2, j - 2), newBoard)) {
                    return true
                }
            }
        }
        for (i in 0..5) {
            for (j in 0..5) {
                if (isMovePossible(Move(p!!, i, j, i + 1, j + 1), newBoard)) {
                    return true
                }
                if (isMovePossible(Move(p, i, j, i - 1, j + 1), newBoard)) {
                    return true
                }
                if (isMovePossible(Move(p, i, j, i + 1, j - 1), newBoard)) {
                    return true
                }
                if (isMovePossible(Move(p, i, j, i - 1, j - 1), newBoard)) {
                    return true
                }
            }
        }
        return false
    }

    //chhecks if an action is possible
    private fun isMovePossible(a: Move, board: Array<Array<String>>): Boolean {
        val player = a.player
        val r1 = a.inRow
        val c1 = a.inCol
        val r2 = a.fiRow
        val c2 = a.fiCol

        if (player.name == "w") {
            if (board[r1][c1] == "w") {
                if ((r1 >= 0 && r1 <= 5) && (c1 >= 0 && c1 <= 5) && (r2 >= 0 && r2 <= 5) && (c2 >= 0 && c2 <= 5)) {
                    if ((board[r2][c2]) === " ") {
                        if ((r2 == r1 - 1 && c2 == c1 + 1) || ((r2 == r1 - 1 && c2 == c1 - 1))) {
                            return true
                        }
                    }
                }
            } else if (board[r1][c1] == "W") {
                if ((r1 >= 0 && r1 <= 5) && (c1 >= 0 && c1 <= 5) && (r2 >= 0 && r2 <= 5) && (c2 >= 0 && c2 <= 5)) {
                    if ((board[r2][c2]) === " ") {
                        if ((r2 == r1 + 1 && c2 == c1 + 1) || ((r2 == r1 + 1 && c2 == c1 - 1)) || (r2 == r1 - 1 && c2 == c1 + 1) || ((r2 == r1 - 1 && c2 == c1 - 1))) {
                            return true
                        }
                    }
                }
            }
        } else if (player.name == "b") {
            if (board[r1][c1] == "b") {
                if ((r1 >= 0 && r1 <= 5) && (c1 >= 0 && c1 <= 5) && (r2 >= 0 && r2 <= 5) && (c2 >= 0 && c2 <= 5)) {
                    if ((board[r2][c2]) === " ") {
                        if ((r2 == r1 + 1 && c2 == c1 + 1) || ((r2 == r1 + 1 && c2 == c1 - 1))) {
                            return true
                        }
                    }
                }
            } else if (board[r1][c1] == "B") {
                if ((r1 >= 0 && r1 <= 5) && (c1 >= 0 && c1 <= 5) && (r2 >= 0 && r2 <= 5) && (c2 >= 0 && c2 <= 5)) {
                    if ((board[r2][c2]) === " ") {
                        if ((r2 == r1 + 1 && c2 == c1 + 1) || ((r2 == r1 + 1 && c2 == c1 - 1)) || (r2 == r1 - 1 && c2 == c1 + 1) || ((r2 == r1 - 1 && c2 == c1 - 1))) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    private fun isJumpPossible(a: Move, board: Array<Array<String>>): Boolean {
        val player = a.player
        val r1 = a.inRow
        val c1 = a.inCol
        val r2 = a.fiRow
        val c2 = a.fiCol

        if (player.name == "w") {
            if (board[r1][c1] == "w") {
                if ((r1 >= 0 && r1 <= 5) && (c1 >= 0 && c1 <= 5) && (r2 >= 0 && r2 <= 5) && (c2 >= 0 && c2 <= 5) && (r2 < r1)) {
                    if ((board[r2][c2]) === " ") {
                        if (board[(r1 + r2) / 2][(c1 + c2) / 2] == "b" || board[(r1 + r2) / 2][(c1 + c2) / 2] == "B") {
                            return true
                        }
                    }
                }
            } else if (board[r1][c1] == "W") {
                if ((r1 >= 0 && r1 <= 5) && (c1 >= 0 && c1 <= 5) && (r2 >= 0 && r2 <= 5) && (c2 >= 0 && c2 <= 5)) {
                    if ((board[r2][c2]) === " ") {
                        if (board[(r1 + r2) / 2][(c1 + c2) / 2] == "b" || board[(r1 + r2) / 2][(c1 + c2) / 2] == "B") {
                            return true
                        }
                    }
                }
            }
        }
        if (player.name == "b") {
            if ((r1 >= 0 && r1 <= 5) && (c1 >= 0 && c1 <= 5) && (r2 >= 0 && r2 <= 5) && (c2 >= 0 && c2 <= 5) && (r2 > r1)) {
                if (board[r1][c1] == "b") {
                    if ((board[r2][c2]) === " ") {
                        if (board[(r1 + r2) / 2][(c1 + c2) / 2] == "w" || board[(r1 + r2) / 2][(c1 + c2) / 2] == "W") {
                            return true
                        }
                    }
                }
            } else if (board[r1][c1] == "B") {
                if ((r1 >= 0 && r1 <= 5) && (c1 >= 0 && c1 <= 5) && (r2 >= 0 && r2 <= 5) && (c2 >= 0 && c2 <= 5)) {
                    if ((board[r2][c2]) === " ") {
                        if (board[(r1 + r2) / 2][(c1 + c2) / 2] == "w" || board[(r1 + r2) / 2][(c1 + c2) / 2] == "W") {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    override fun toString(): String {
        return AsciiTable(AT_Context().apply {
            setWidth(6 * 5)
        }).apply {
            addRule()
            addRow("", *number)
            addRule()
            letter.forEachIndexed { index, c ->
                addRow(c, *Board[index])
                addRule()
            }
        }.render().plus("\n")
    }

    fun Win(p: Player): Boolean {
        if (p.name == "b") {
            for (i in Board.indices) {
                for (j in Board.indices) {
                    if (Board[i][j] == "w" || Board[i][j] == "W") {
                        return false
                    }
                }
            }
            println("Чёрные выигрывают, так как у белых больше не осталось шашек.")
        } else if (p.name == "w") {
            for (i in Board.indices) {
                for (j in Board.indices) {
                    if (Board[i][j] == "b" || Board[i][j] == "B") {
                        return false
                    }
                }
            }
            println("Белые выигрывают, так как у чёрных больше не осталось шашек.")
        }
        return true
    }

    fun HasWon(p: Player, s: State): Boolean {
        if (p.name == "b") {
            for (i in 0..5) {
                for (j in 0..5) {
                    if (s.Board[i][j] == "w" || s.Board[i][j] == "W") {
                        return false
                    }
                }
            }
        } else {
            for (i in 0..5) {
                for (j in 0..5) {
                    if (s.Board[i][j] == "b" || s.Board[i][j] == "B") {
                        return false
                    }
                }
            }
        }
        return true
    }

    companion object {
        var w: String = "w"
        var wK: String = "W"
        var b: String = "b"
        var bK: String = "B"


    }
}