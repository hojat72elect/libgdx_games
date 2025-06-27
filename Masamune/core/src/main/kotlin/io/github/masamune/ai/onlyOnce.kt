package io.github.masamune.ai

import com.badlogic.gdx.ai.btree.Decorator
import com.badlogic.gdx.ai.btree.Task
import ktx.ai.GdxAiDsl
import ktx.app.gdxError
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class OnlyOnce<E> : Decorator<E>() {
    private var executed = false

    override fun run() {
        if (executed) {
            child.fail()
            return
        }

        super.run()
    }

    override fun childSuccess(runningTask: Task<E>?) {
        success()
        executed = true
    }
}

@OptIn(ExperimentalContracts::class)
@GdxAiDsl
inline fun <E> Task<E>.onlyOnce(
    init: (@GdxAiDsl OnlyOnce<E>).() -> Unit = {},
): Int {
    contract { callsInPlace(init, InvocationKind.EXACTLY_ONCE) }
    val decorator = OnlyOnce<E>()
    decorator.init()
    if (decorator.childCount == 0) {
        gdxError("onlyOnce decorator must have at least 1 child")
    }
    return addChild(decorator)
}
