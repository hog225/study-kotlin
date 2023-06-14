



fun main() {
    fun getName(): String {
        return "hi"
    }
    val name: String by lazy(::getName)
    println(name)

    var i: String? = null
    fun add(k: Int): Int {
        return k + 1;
    }

    i?.let {  }
    add(2).run { this.plus(3) }
    add(2).let { it.plus(3) }

}
