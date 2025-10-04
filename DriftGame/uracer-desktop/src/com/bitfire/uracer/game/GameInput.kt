package com.bitfire.uracer.game

import com.badlogic.gdx.Input.Keys
import com.bitfire.uracer.Input
import com.bitfire.uracer.configuration.UserPreferences.Preference
import com.bitfire.uracer.configuration.UserPreferences.string
import com.bitfire.uracer.game.GameplaySettings.TimeDilateInputMode
import com.bitfire.uracer.game.logic.gametasks.messager.Message

class GameInput(private val logic: GameLogic, private val input: Input) {

    private val timeMode = TimeDilateInputMode.valueOf(string(Preference.TimeDilateInputMode))
    var isTimeDilating = false

    init {
        input.releaseAllKeys()
    }

    fun resetTimeDilating() {
        this.isTimeDilating = false
    }

    fun ensureConsistenceAfterResume() {
        // In case the input mode is set to TouchAndRelease then the keyup/button-released event may have
        // been already triggered during the pause, check for it and disable time dilation if it's the case.
        if (timeMode == TimeDilateInputMode.TouchAndRelease) {
            if (!input.isOn(Keys.SPACE) && !input.isTouching(Input.MouseButton.Right)) {
                logic.endTimeDilation()
            }
        }
    }

    fun update() {
        if (input.isPressed(Keys.R)) {
            logic.restartGame()
            logic.showMessage("Restarted", 1.5f, Message.Type.Information, Message.Position.Bottom, Message.Size.Big)
        } else if (input.isPressed(Keys.T)) {
            logic.resetGame()
            logic.showMessage("Reset", 1.5f, Message.Type.Information, Message.Position.Bottom, Message.Size.Big)
        } else if (input.isPressed(Keys.TAB)) {
            // choose next/prev best target
            val backward = input.isOn(Keys.SHIFT_LEFT) || input.isOn(Keys.SHIFT_RIGHT)
            logic.chooseNextTarget(backward)
        }

        val rightMouseButton = input.isTouched(Input.MouseButton.Right)

        when (timeMode) {
            TimeDilateInputMode.Toggle -> if (input.isPressed(Keys.SPACE) || rightMouseButton) {
                this.isTimeDilating = !this.isTimeDilating

                if (this.isTimeDilating) {
                    if (logic.isTimeDilationAvailable) logic.startTimeDilation() else this.isTimeDilating = false
                } else {
                    logic.endTimeDilation()
                }
            }

            TimeDilateInputMode.TouchAndRelease -> if (input.isPressed(Keys.SPACE) || rightMouseButton) {
                if (!this.isTimeDilating && logic.isTimeDilationAvailable) {
                    this.isTimeDilating = true
                    logic.startTimeDilation()
                }
            } else if (input.isReleased(Keys.SPACE) || input.isUntouched(Input.MouseButton.Right)) {
                if (this.isTimeDilating) {
                    this.isTimeDilating = false
                    logic.endTimeDilation()
                }
            }
        }
    }
}
