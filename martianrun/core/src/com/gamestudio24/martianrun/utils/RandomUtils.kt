package com.gamestudio24.martianrun.utils

import com.gamestudio24.martianrun.enums.EnemyType
import java.util.Random

object RandomUtils {
    val randomEnemyType: EnemyType?
        /**
         * @return a random [EnemyType]
         */
        get() {
            val randomEnum = RandomEnum(EnemyType::class.java)
            return randomEnum.random()
        }

    /**
     * @see [Stack Overflow](http://stackoverflow.com/a/1973018)
     */
    private class RandomEnum<E : Enum<*>>(token: Class<E>) {
        private val values: Array<E> = token.getEnumConstants()

        /**
         * @return a random value for the given enum
         */
        fun random(): E {
            return values[RND.nextInt(values.size)]
        }

        companion object {
            private val RND = Random()
        }
    }
}
