import java.util.*

// For making moves you can see that each column is represented by numbers(1-6) and each row is represented by
// letters(A-H). Hence you can make moves like “G6-F5” or “F5-E6”, without quotes.
object TestClass {
    @JvmStatic
    fun main(args: Array<String>) {
        var player1 = ""
        var player2 = ""
        var move = ""
        val sc = Scanner(System.`in`)
        println("Which piece do you prefer - Black(b) or White(w)?")
        player1 = sc.nextLine()
        while (player1 != "b" && player1 != "w") {
            println("Would you like to play b(b) or w(w)?")
            player1 = sc.nextLine()
        }
        println("You're $player1")
        val pHuman = Player(player1)
        player2 = if (player1 == "w") {
            "b"
        } else {
            "w"
        }
        println(
            """
                For making moves you can see that each column is represented by numbers(1-6) and each row is represented by
                letters(A-H). Hence you can make moves like “G6-F5” or “F5-E6”, without quotes.
                """.trimIndent()
        )
        val pAI = Player(player2)

        val board = Array(6) {
            Array(6) { " " }
        }
        val s = State(board, pHuman)
        s.start()
        s.fillMap()
        println(s.toString())
        var next: State

        println("Black Starts First")
        val Node: MinMax_AB = MinMax_AB()
        while (true) {
            println("Black's Turn! Make a Move: ")
            move = sc.nextLine()
            while (s.isValidInput(pHuman, move) == false) {
                move = sc.nextLine()
            }
            val human = s.getInput(pHuman, move)
            next = s.applyMove(human, s)
            println(s.toString())
            if (s.Win(pHuman)) {
                System.exit(0)
            }
            if (!s.AnyMovePossible(pAI, s)) {
                println("No more moves possible! Game over!")
                System.exit(0)
            }
            next = Node.MinMax(pAI, next)
            s.updateBoard(next)
            println(s.toString())
            if (s.Win(pAI)) {
                System.exit(0)
            }
            if (!s.AnyMovePossible(pHuman, s)) {
                println("No more moves possible! Game over!")
                System.exit(0)
            }
        }
    }
}