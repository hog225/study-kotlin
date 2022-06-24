package util

sealed class Either<out A, out B> {
    internal class Left<out A, out B>(private val value: A): Either<A, B>() {
        override fun toString(): String {
            return "Left($value)"
        }
    }

    internal class Right<out A, out B>(private val value: B): Either<A, B>() {
        override fun toString(): String {
            return "Right($value)"
        }
    }

    companion object {
        fun <A, B> left(value: A): Either<A, B> = Left(value)
        fun <A, B> right(value: B): Either<A, B> = Right(value)


    }



}

fun main(array: Array<String>) {
    val result = Either.left<Int, Int>(3)
    println(result)
}