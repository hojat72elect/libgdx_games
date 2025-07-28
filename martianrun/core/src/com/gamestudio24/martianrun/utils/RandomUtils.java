
package com.gamestudio24.martianrun.utils;

import com.gamestudio24.martianrun.enums.EnemyType;

import java.util.Random;

public class RandomUtils {

    /**
     * @return a random {@link com.gamestudio24.martianrun.enums.EnemyType}
     */
    public static EnemyType getRandomEnemyType() {
        RandomEnum<EnemyType> randomEnum = new RandomEnum<EnemyType>(EnemyType.class);
        return randomEnum.random();
    }

    /**
     * @see <a href="http://stackoverflow.com/a/1973018">Stack Overflow</a>
     */
    private static class RandomEnum<E extends Enum> {

        private static final Random RND = new Random();
        private final E[] values;

        public RandomEnum(Class<E> token) {
            values = token.getEnumConstants();
        }

        /**
         * @return a random value for the given enum
         */
        public E random() {
            return values[RND.nextInt(values.length)];
        }
    }

}
