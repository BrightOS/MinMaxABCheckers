import kotlin.math.max
import kotlin.math.min

class MinMax_AB {

    private var minMaxMatrix: MutableList<ValuedMove> = mutableListOf()
    private var chosenMove: Move? = null

    fun heuristic(state: State): Int {
        val board = state.Board
        val playerName = state.p.name

        var enemyCheckersAtMy = 0 // Количество вражеских шашек на моей половине поля
        var eatenEnemyCheckers = 0 // Количество съеденных шашек соперника
        var myLadies = 0 // Количество моих дамок
        var lockedCheckers = 0 // Количество запертых белых/чёрных шашек

        eatenEnemyCheckers = 8 - when {
            playerName == "w" -> {
                board.sumOf { it.count { it == "b" || it == "B" } }
            }
            else -> {
                board.sumOf { it.count { it == "w" || it == "W" } }
            }
        }

        for (i in board.indices) {
            for (j in board.indices) {
                if (board[i][j] == "b" && i > 2 && playerName == "w") {
                    enemyCheckersAtMy++
                }
                if (board[i][j] == "w" && i < 3 && playerName == "b") {
                    enemyCheckersAtMy++
                }
                if (board[i][j] == "B" && playerName == "b") {
                    myLadies++
                }
                if (board[i][j] == "W" && playerName == "w") {
                    myLadies++
                }
                if (board[i][j] == "w" && playerName == "w") {
                    if (!state.AnyMovePossible(state.p, state)) lockedCheckers++
                }
                if (board[i][j] == "b" && playerName == "b") {
                    if (!state.AnyMovePossible(state.p, state)) lockedCheckers++
                }
                if (board[i][j] == "W") {
                    lockedCheckers++
                }
            }
        }
        val h = when {
            playerName == "w" -> ((enemyCheckersAtMy * 2) + (eatenEnemyCheckers) + (myLadies * 3) + (lockedCheckers * 3))
            else -> ((enemyCheckersAtMy * 3) + (eatenEnemyCheckers * 3) + (myLadies * 2) + (lockedCheckers * 3))
        }

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