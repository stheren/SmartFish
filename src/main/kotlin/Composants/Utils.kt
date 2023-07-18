package Composants

import javafx.scene.layout.Region

object Utils
{
    const val VALUE: Int    = 16
    const val MAP_SIZE: Int = 30

    fun Region.setExclusiveSize(width: Double, height: Double)
    {
        this.minWidth   = width
        this.maxWidth   = width
        this.prefWidth  = width
        this.minHeight  = height
        this.maxHeight  = height
        this.prefHeight = height
    }
}
