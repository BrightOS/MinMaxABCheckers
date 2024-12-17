fun main() {
    val firstAIPlayer = Player("w")
    val secondAIPlayer = Player("b")

    val board = Array(6) {
        Array(6) { " " }
    }
    val s = State(board, firstAIPlayer)
    s.start()
    s.fillMap()
    println("Начальное состояние игрового поля:")
    println(s.toString())
    var next: State = s

    val Node = MinMax_AB()
    var currentIteration = 1
    while (true) {
        println("Ход белых №$currentIteration.")
        next = Node.MinMax(firstAIPlayer, next)
        s.updateBoard(next)
        println(s.toString())
        if (s.Win(firstAIPlayer)) {
            System.exit(0)
        }
        if (!s.AnyMovePossible(secondAIPlayer, s)) {
            println("У чёрных больше нет доступных ходов. Игра окончена! Победа белых!")
            System.exit(0)
        }
        println("Ход чёрных №${currentIteration++}.")
        next = Node.MinMax(secondAIPlayer, next)
        s.updateBoard(next)
        println(s.toString())
        if (s.Win(secondAIPlayer)) {
            System.exit(0)
        }
        if (!s.AnyMovePossible(firstAIPlayer, s)) {
            println("У белых больше нет доступных ходов. Игра окончена! Победа чёрных!")
            System.exit(0)
        }
    }
}