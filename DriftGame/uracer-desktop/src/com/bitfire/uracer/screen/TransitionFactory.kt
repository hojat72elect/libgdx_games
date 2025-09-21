package com.bitfire.uracer.screen

import com.badlogic.gdx.utils.LongMap
import com.bitfire.uracer.screen.transitions.CrossFader
import com.bitfire.uracer.screen.transitions.Fader
import com.bitfire.utils.Hash

object TransitionFactory {

    private val transitions = LongMap<ScreenTransition>()
    private lateinit var screenFactory: ScreenFactory

    @JvmStatic
    fun init(factory: ScreenFactory) {
        screenFactory = factory
    }

    @JvmStatic
    fun getTransition(transitionType: TransitionType): ScreenTransition {
        var transition = transitions.get(transitionType.hash)

        if (transition == null) {
            transition = createTransition(transitionType)
            transitions.put(transitionType.hash, transition)
        } else {
            transition.reset()
        }

        return transition
    }

    private fun createTransition(transitionType: TransitionType) = when (transitionType) {
        TransitionType.CrossFader -> CrossFader(screenFactory)
        TransitionType.Fader -> Fader(screenFactory)
        TransitionType.None -> null
    }

    @JvmStatic
    fun dispose() {
        for (transition in transitions.values()) transition.dispose()
    }

    enum class TransitionType {
        None, CrossFader, Fader;

        var hash = Hash.apHash(this.name)
    }
}
