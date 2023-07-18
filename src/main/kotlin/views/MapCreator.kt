package views

import Composants.Utils.VALUE
import Composants.Utils.setExclusiveSize
import SpendYourTime.Images.Images
import SpendYourTime.models.Map
import javafx.animation.AnimationTimer
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.ComboBox
import javafx.scene.control.Dialog
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.*


class MapCreator private constructor() : StackPane()
{
    companion object
    {
        var instance: MapCreator = MapCreator()
    }

    enum class Layer
    {
        floor, first, second
    }

    private val view                = Canvas(500.0, 500.0)
    private val gc: GraphicsContext = view.graphicsContext2D

    private var xOffsetView = 0.0
    private var yOffsetView = 0.0

    private var mouseX = 0.0
    private var mouseY = 0.0

    private var selectedNumber = 1
    private var selectedLayer  = Layer.floor

    init
    {
        VBox.setVgrow(this, Priority.ALWAYS)

        style      = "-fx-background-color: #2c2f31;"
        view.style = "-fx-background-color: #FFFFFF;"

        children.add(view)

        val ui       = Image(javaClass.getResourceAsStream("/assets/UI.png"))
        val selector = WritableImage(ui.pixelReader, VALUE.toInt(), 0, VALUE.toInt(), VALUE.toInt())

        view.onMouseMoved = EventHandler { event ->
            mouseX = event.x
            mouseY = event.y
        }

        view.onMousePressed = EventHandler { event ->
            if (event.isPrimaryButtonDown)
            {
                // Map.instance.set((event.x / 16).toInt(), (event.y / 16).toInt(), 1);
                when (selectedLayer)
                {
                    Layer.floor -> Map.instance.setFloor(
                            (event.x / VALUE).toInt(),
                            (event.y / VALUE).toInt(),
                            selectedNumber
                                                        )

                    Layer.first -> Map.instance.setFirstLayer(
                            (event.x / VALUE).toInt(),
                            (event.y / VALUE).toInt(),
                            selectedNumber
                                                             )

                    Layer.second -> Map.instance.setSecondLayer(
                            (event.x / VALUE).toInt(),
                            (event.y / VALUE).toInt(),
                            selectedNumber
                                                               )
                }
            }
            else if (event.isSecondaryButtonDown)
            {
                xOffsetView = view.translateX - event.screenX
                yOffsetView = view.translateY - event.screenY
            }
        }

        view.translateY = -20.0

        view.onMouseDragged = EventHandler { event ->
            if (event.isSecondaryButtonDown)
            {
                view.translateX = event.screenX + xOffsetView
                view.translateY = event.screenY + yOffsetView
            }
        }

        view.scaleX = 0.95
        view.scaleY = 0.95

        view.onScroll = EventHandler { event ->
            if (event.deltaY > 0)
            {
                view.scaleX += if (view.scaleX < 10.0) 0.1 else 0.0
                view.scaleY += if (view.scaleY < 10.0) 0.1 else 0.0
            }
            else
            {
                view.scaleX -= if (view.scaleX > 0.5) 0.1 else 0.0
                view.scaleY -= if (view.scaleY > 0.5) 0.1 else 0.0
            }
        }

        view.onMouseEntered = EventHandler { _ ->
            scene.cursor = Cursor.HAND;
        }

        view.onMouseExited = EventHandler { _ ->
            scene.cursor = Cursor.DEFAULT;
        }

        object : AnimationTimer()
        {
            override fun handle(currentNanoTime: Long)
            {
                // Clear the canvas
                gc.clearRect(0.0, 0.0, view.width, view.height)

                // Draw the UI
                Map.instance._floor.forEachIndexed { i, list ->
                    list.forEachIndexed { j, value ->
                        gc.drawImage(Images.room[value], (i * VALUE).toDouble(), (j * VALUE).toDouble())
                    }
                }

                Map.instance._firstLayer.forEachIndexed { i, list ->
                    list.forEachIndexed { j, value ->
                        if (value != 0)
                        {
                            gc.drawImage(Images.office[value], (i * VALUE).toDouble(), (j * VALUE).toDouble())
                        }
                    }
                }

                Map.instance._secondLayer.forEachIndexed { i, list ->
                    list.forEachIndexed { j, value ->
                        if (value != 0)
                        {
                            gc.drawImage(Images.office[value], (i * VALUE).toDouble(), (j * VALUE).toDouble())
                        }
                    }
                }

                gc.drawImage(
                        selector, mouseX - (mouseX % 16), mouseY - (mouseY % 16), VALUE.toDouble(),
                        VALUE.toDouble()
                            )
            }
        }.start()


        // Create bottom bar
        val bottomBar = HBox().apply {
            style     = "-fx-background-color: #3c3f41;"
            maxHeight = Double.MIN_VALUE

            setAlignment(this, Pos.BOTTOM_CENTER)

            alignment = Pos.CENTER
            spacing   = 50.0

            children.add(HBox().apply
            {
                children.add(Button("Save").apply
                {
                    setExclusiveSize(100.0, 25.0)

                    setOnAction {
                        // TODO: Save the map
                    }
                })
                children.add(Button("Change").apply
                {
                    setExclusiveSize(100.0, 25.0)

                    setOnAction {
                        val dialog: Dialog<Pair<String, String>> = Dialog()

                        dialog.title = "Change the tile"

                        dialog.dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)

                        val grid     = GridPane()
                        grid.hgap    = 1.0
                        grid.vgap    = 1.0
                        grid.padding = Insets(20.0, 150.0, 10.0, 10.0)
                        grid.style   = "-fx-background-color: #2c2f31;"

                        var tiles    = mutableListOf<Button>()
                        var selected = 0
                        var images   = when
                        {
                            selectedLayer == Layer.floor -> Images.room
                            else                         -> Images.office
                        }

                        for (i in 0 until images.size)
                        {
                            tiles.add(Button().apply
                            {
                                setExclusiveSize(16.0, 16.0)

                                this.style = "-fx-background-color: transparent; -fx-border-color: transparent;"

                                var img: WritableImage = images[i]
                                val trf                = ImageView(img)
                                graphic                = trf

                                grid.add(this, i % 16, i / 16)
                                setOnAction {
                                    selected = i
                                    dialog.close()
                                }
                            })
                        }
                        dialog.dialogPane.content = grid

                        dialog.showAndWait()

                        selectedNumber = selected
                    }
                })
                children.add(ComboBox<Layer>().apply
                {
                    setExclusiveSize(100.0, 25.0)

                    items.addAll(Layer.values())

                    selectionModel.select(0)

                    selectionModel.selectedItemProperty().addListener { _, _, newValue ->
                        selectedLayer = newValue
                    }
                })
            })

        }
        children.add(bottomBar)
    }
}

