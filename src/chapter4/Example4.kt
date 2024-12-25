package chapter4

import java.io.Serializable

interface State: Serializable

interface View {
    fun getCurrentState(): State

    fun restoreState(state: State) {}
}

class Button2 : View {

    override fun getCurrentState(): State = ButtonState()

    override fun restoreState(state: State) {}

    class ButtonState : State {}

}