import kotlin.math.max
import kotlin.math.min

class MinMax_AB {

    private var minMaxMatrix: MutableList<ValuedMove> = mutableListOf()
    private var chosenMove: Move? = null

    fun heuristic(state: State): Int {
        val board = state.Board
        val playerName = state.p
        val isBlack = if (playerName.name == "b") -1 else 1

        var cb = 0
        var cw = 0
        var cbk = 0
        var cwk = 0
        for (i in 0 until board.size) {
            for (j in 0 until board.size) {
                if (board[i][j].equals("b")) {
                    cb++
                }
                if (board[i][j].equals("B")) {
                    cbk++
                }
                if (board[i][j].equals("w")) {
                    cw++
                }
                if (board[i][j].equals("W")) {
                    cwk++
                }
            }
        }
        val h = ((cb) * (10 * isBlack) + ((cw) * (-10 * isBlack)) + (cbk * 20 * isBlack) + (cwk * (-20 * isBlack)))

        return h
    }

    fun MinMax(p: Player, s: State): State {
        minMaxMatrix.clear()
        val value = getMax(p, s, 0, Int.MIN_VALUE, Int.MAX_VALUE)
        minMaxMatrix
            .groupBy { it.moveMax }
            .map {
                "${it.key.toPerfectString(s)}: ${
                    it.value.map { "${it.moveMin.toPerfectString(s)} (${it.value})" }.toSet().joinToString(", ")
                        .let { str ->
                            if (it.key == chosenMove) str.plus(" <- Выбираем") else str
                        }
                }"
            }
            .forEach {
                println(it)
            }
        println()
        println("Выбран шаг: ${chosenMove?.toPerfectString(s)}")
        println()
        return value
    }

    fun changePlayer(p: Player): Player {
        return if (p.name.equals("w")) {
            Player("b")
        } else {
            Player("w")
        }
    }

    fun getMax(p: Player, s: State, d: Int, alpha: Int, beta: Int): State {
        var alpha = alpha
        val moves: ArrayList<Move> = s.NextMoves(p, s)
        val children: ArrayList<State> = s.NextStates(moves, s)
        if ((s.HasWon(p, s)) || (d > 2) || (children.size == 0)) {
            return s
        }
        var maxUtil = Int.MIN_VALUE
        var maxNode: State = children[0]
        var _break = false
        children.forEachIndexed { index, c ->
            if (_break) return@forEachIndexed
            val util = heuristic(getMin(changePlayer(p), c, d + 1, alpha, beta, moves[index]))
            if (util > maxUtil) {
                maxUtil = util
                maxNode = c
                chosenMove = moves[index]
            }
            alpha = max(alpha.toDouble(), maxUtil.toDouble()).toInt()
            if (beta <= alpha) {
                _break = true
            }
        }
        return maxNode
    }

    fun getMin(p: Player, s: State, d: Int, alpha: Int, beta: Int, moveMax: Move): State {
        var beta = beta
        val moves: ArrayList<Move> = s.NextMoves(p, s)
        val children: ArrayList<State> = s.NextStates(moves, s)
        if (s.HasWon(p, s) || (d > 6) || (children.size == 0)) {
            return s
        }
        var minUtil = Int.MAX_VALUE
        var minNode: State = children[0]
        var _break = false
        children.forEachIndexed { index, c ->
            if (_break) return@forEachIndexed
            val util = heuristic(c)
            if (util <= minUtil) {
                minUtil = util
                minNode = c
            }
            minMaxMatrix.add(ValuedMove(value = util, moveMax = moveMax, moveMin = moves[index]))
            beta = min(beta.toDouble(), minUtil.toDouble()).toInt()
            if (beta <= alpha) {
                _break = true
            }
        }
        return minNode
    }
}