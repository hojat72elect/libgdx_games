package com.salvai.snake.input;

import com.salvai.snake.enums.MovingDirection;
import com.salvai.snake.utils.Constants;

public class SwipeDetector {


    public static MovingDirection onSwipe(int velocityX, int velocityY) {
        if (Math.abs(velocityX) > Math.abs(velocityY) && Math.abs(velocityX) > Constants.SWIPE_FACTOR)
            if (velocityX > 0)
                return MovingDirection.RIGHT;
            else
                return MovingDirection.LEFT;
        else if (Math.abs(velocityY) > Constants.SWIPE_FACTOR)
            if (velocityY > 0)
                return MovingDirection.DOWN;
            else
                return MovingDirection.UP;
        return null;
    }
}