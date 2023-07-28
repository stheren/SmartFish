package views

import Composants.Utils.VALUE
import Composants.Utils.setExclusiveSize
import SpendYourTime.Images.Images
import SpendYourTime.models.Case
import SpendYourTime.models.Map_Syp
import WindowsAfk
import com.fasterxml.jackson.databind.ObjectMapper
import javafx.animation.AnimationTimer
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.FileChooser


class MapCreator private constructor() : StackPane() {
    companion object {
        var instance: MapCreator = MapCreator()
    }

    enum class Layer {
        floor, first, second, hitbox
    }

    private val view = Canvas(500.0, 500.0)
    private val gc: GraphicsContext = view.graphicsContext2D

    private var xOffsetView = 0.0
    private var yOffsetView = 0.0

    private var mouseX = 0.0
    private var mouseY = 0.0

    private var selectedImage = 0
    private var selectedNumber = 1
    private var selectedLayer = Layer.floor
    private var hitboxValue = 0
    private var newWidth = 30
    private var newHeight = 30

    var displayFloor: Boolean = true
    var displayFirstLayer: Boolean = true
    var displaySecondLayer: Boolean = true
    var displayHitbox: Boolean = true

    var Map_Syp = Map_Syp()

    var information: Label = Label("")

    init {
        VBox.setVgrow(this, Priority.ALWAYS)
        style = "-fx-background-color: #2c2f31;"

        view.style = "-fx-background-color: #FFFFFF;"
        children.add(view)

        val ui = Image(javaClass.getResourceAsStream("/assets/UI.png"))
        val selector = WritableImage(ui.pixelReader, VALUE.toInt(), 0, VALUE.toInt(), VALUE.toInt())

        view.onMouseMoved = EventHandler { event ->
            mouseX = event.x
            mouseY = event.y
        }

        view.onMousePressed = EventHandler { event ->
            if (event.isPrimaryButtonDown) {
                // Map.instance.set((event.x / 16).toInt(), (event.y / 16).toInt(), 1);
                when (selectedLayer) {
                    Layer.floor -> Map_Syp.setFloor(
                        (event.x / VALUE).toInt(),
                        (event.y / VALUE).toInt(),
                        Case(selectedImage, selectedNumber)
                    )

                    Layer.first -> Map_Syp.setFirstLayer(
                        (event.x / VALUE).toInt(),
                        (event.y / VALUE).toInt(),
                        Case(selectedImage, selectedNumber)
                    )

                    Layer.second -> Map_Syp.setSecondLayer(
                        (event.x / VALUE).toInt(),
                        (event.y / VALUE).toInt(),
                        Case(selectedImage, selectedNumber)
                    )

                    Layer.hitbox -> Map_Syp.setHitbox(
                        (event.x / VALUE).toInt(),
                        (event.y / VALUE).toInt(),
                        hitboxValue
                    )
                }
            } else if (event.isSecondaryButtonDown) {
                xOffsetView = view.translateX - event.screenX
                yOffsetView = view.translateY - event.screenY
            }
        }

        view.translateY = -20.0
        view.onMouseDragged = EventHandler { event ->
            if (event.isSecondaryButtonDown) {
                view.translateX = event.screenX + xOffsetView
                view.translateY = event.screenY + yOffsetView
            }
            if (event.isPrimaryButtonDown) {
                when (selectedLayer) {
                    Layer.floor -> Map_Syp.setFloor(
                        (event.x / VALUE).toInt(),
                        (event.y / VALUE).toInt(),
                        Case(selectedImage, selectedNumber)
                    )

                    Layer.first -> Map_Syp.setFirstLayer(
                        (event.x / VALUE).toInt(),
                        (event.y / VALUE).toInt(),
                        Case(selectedImage, selectedNumber)
                    )

                    Layer.second -> Map_Syp.setSecondLayer(
                        (event.x / VALUE).toInt(),
                        (event.y / VALUE).toInt(),
                        Case(selectedImage, selectedNumber)
                    )

                    Layer.hitbox -> Map_Syp.setHitbox(
                        (event.x / VALUE).toInt(),
                        (event.y / VALUE).toInt(),
                        hitboxValue
                    )
                }
            }
        }

        view.scaleX = 0.95
        view.scaleY = 0.95
        view.onScroll = EventHandler { event ->
            if (event.deltaY > 0) {
                view.scaleX += if (view.scaleX < 10.0) 0.1 else 0.0
                view.scaleY += if (view.scaleY < 10.0) 0.1 else 0.0
            } else {
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

        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                // Clear the canvas
                gc.clearRect(0.0, 0.0, view.width, view.height)

                // Draw the UI
                if (displayFloor) {
                    Map_Syp.getFloor().forEachIndexed { i, list ->
                        list.forEachIndexed { j, value ->
                            gc.drawImage(Images.get(value), (i * VALUE).toDouble(), (j * VALUE).toDouble())
                        }
                    }
                }

                if (displayFirstLayer) {
                    Map_Syp.getFirstLayer().forEachIndexed { i, list ->
                        list.forEachIndexed { j, value ->
                            if (Images.isCorrect(value)) {
                                gc.drawImage(Images.get(value), (i * VALUE).toDouble(), (j * VALUE).toDouble())
                            }
                        }
                    }
                }

                if (displaySecondLayer) {
                    Map_Syp.getSecondLayer().forEachIndexed { i, list ->
                        list.forEachIndexed { j, value ->
                            if (Images.isCorrect(value)) {
                                gc.drawImage(Images.get(value), (i * VALUE).toDouble(), (j * VALUE).toDouble())
                            }
                        }
                    }
                }

                if (displayHitbox) {
                    Map_Syp.getHitbox().forEachIndexed { i, list ->
                        list.forEachIndexed { j, value ->
                            // Red square if the case is not walkable with some alpha value
                            when (value) {
                                0 -> gc.fill = Color.rgb(0, 255, 0, 0.1)
                                1 -> gc.fill = Color.rgb(255, 0, 0, 0.1)
                                2 -> gc.fill = Color.rgb(0, 0, 255, 0.1)
                                3 -> gc.fill = Color.rgb(255, 255, 0, 0.1)
                                else -> gc.fill = Color.rgb(255, 255, 255, 0.5)
                            }
                            gc.fillRect(
                                (i * VALUE).toDouble(),
                                (j * VALUE).toDouble(),
                                VALUE.toDouble(),
                                VALUE.toDouble()
                            )
                        }

                    }
                }

                gc.drawImage(
                    selector, mouseX - (mouseX % 16), mouseY - (mouseY % 16), VALUE.toDouble(),
                    VALUE.toDouble()
                )

                information.text = "Map size : ${Map_Syp.getWidth()} x ${Map_Syp.getHeight()}\n"
            }
        }.start()


        // Create Left bar
        val leftBar = VBox().apply {
            style = "-fx-background-color: #3c3f41;"
//            maxHeight = Double.MIN_VALUE
            maxWidth = 320.0
            setAlignment(this, Pos.CENTER_RIGHT)
            alignment = Pos.CENTER_LEFT
            spacing = 10.0

            children.add(HBox().apply {
                alignment = Pos.CENTER_LEFT
                spacing = 10.0
                children.add(information.apply {
                    style = "-fx-text-fill: red; -fx-font-size: 20px; -fx-font-weight: bold;"
                })
            })

            children.add(ScrollPane().apply {
                maxHeight = 400.0

                val grid = GridPane()
                this.content = grid

                grid.hgap = 1.0
                grid.vgap = 1.0
                grid.padding = Insets(0.0)
                grid.style = "-fx-background-color: #2c2f31;"

                var tiles = mutableListOf<Button>()
                var cpt = 0
                Images.getNames().forEachIndexed { ressource, _ ->
                    Images.get(ressource).forEachIndexed { index, image ->
                        grid.add(Button().apply {
                            setExclusiveSize(18.0, 18.0)
                            this.style = "-fx-background-color: transparent; -fx-border-color: transparent;"
                            graphic = ImageView(image)
                            setOnAction {
                                selectedNumber = index
                                selectedImage = ressource
                                // Change graphic of the selected button with a red border
                                tiles.forEach {
                                    it.style = "-fx-background-color: transparent; -fx-border-color: transparent;"
                                }
                                this.style =
                                    "-fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 2px;"

                            }
                            tiles.add(this)
                        }, cpt % 16, cpt / 16)
                        cpt++
                    }
                }
            })

            val radioGroup = ToggleGroup()
            fun createLine(name: String, layer: Layer, selected : Boolean, action : (Boolean) -> Unit) = HBox().apply {
                padding = Insets(0.0, 10.0, 0.0, 10.0)
                alignment = Pos.CENTER_LEFT
                children.add(CheckBox().apply {
                    isSelected = true
                    style = "-fx-text-fill: white;"
                    text = name
                    setOnAction {
                        action(isSelected)
                    }
                })
                children.add(HBox().apply {
                    alignment = Pos.CENTER_RIGHT
                    children.add(RadioButton().apply {
                        setExclusiveSize(50.0, 25.0)
                        isSelected = selected
                        text = "Edit"
                        style = "-fx-text-fill: white;"
                        setOnAction {
                            selectedLayer = layer
                        }
                        toggleGroup = radioGroup
                    })
                    HBox.setHgrow(this, Priority.ALWAYS)
                })
            }

            // Refactor choose layer and display layer by a line with title and button and a checkbox
            children.addAll(
                createLine("Floor", Layer.floor, true) { displayFloor = it },
                createLine("First Layer", Layer.first, false) { displayFirstLayer = it },
                createLine("Second Layer", Layer.second, false) { displaySecondLayer = it },
                createLine("Hitbox", Layer.hitbox, false) { displayHitbox = it }
            )

            fun createTextField(value : Int, action : (Int) -> Unit) = TextField().apply {
                setExclusiveSize(100.0, 25.0)
                text = value.toString()
                // Change on value change
                textProperty().addListener { _, _, newValue ->
                    if (newValue.isEmpty()) {
                        return@addListener
                    }
                    if (newValue.matches(Regex("[0-9]*"))) {
                        action(newValue.toInt())
                        text = newValue
                    } else {
                        text = value.toString()
                    }
                }
            }

            // Add Textfield to change the value of hitbox
            children.add(HBox().apply {
                spacing = 10.0
                children.add(Label("Hitbox value").apply {
                    style = "-fx-text-fill: white;"
                    setExclusiveSize(100.0, 25.0)
                })
                children.add(createTextField(hitboxValue) { hitboxValue = it }.apply {
                    setExclusiveSize(200.0, 25.0)
                })
            })

            children.add(HBox().apply {
                children.add(createTextField(newWidth) { newWidth = it })
                children.add(createTextField(newHeight) { newHeight = it })
                children.add(Button("Recreate").apply {
                    setExclusiveSize(100.0, 25.0)
                    setOnAction {
                        // Open validation dialog
                        val alert = Alert(Alert.AlertType.CONFIRMATION)
                        alert.title = "Recreate map"
                        alert.headerText = "You are about to recreate the map"
                        alert.contentText = "All changes will be lost, are you sure ?"
                        if(alert.showAndWait().get() == ButtonType.CANCEL) {
                            return@setOnAction
                        }

                        Map_Syp.clear()
                        newWidth = if(newWidth > 30) 30 else newWidth
                        newHeight = if(newHeight > 30) 30 else newHeight
                        Map_Syp.create(newWidth, newHeight)
                        view.width = Map_Syp.getWidth() * VALUE.toDouble()
                        view.height = Map_Syp.getHeight() * VALUE.toDouble()
                    }
                })
            })

            children.add(HBox().apply {
                alignment = Pos.CENTER
                spacing = 10.0
                children.add(Button("Save").apply {
                    setExclusiveSize(100.0, 25.0)
                    setOnAction {
                        // Open select file dialog
                        val fileChooser = FileChooser()
                        fileChooser.title = "Save Resource File"
                        val file = fileChooser.showSaveDialog(WindowsAfk.pStage)
                        val mapper = ObjectMapper()
                        mapper.writeValue(file, Map_Syp)
                    }
                })
                children.add(Button("Load").apply {
                    setExclusiveSize(100.0, 25.0)
                    setOnAction {
                        // Open select file dialog
                        val fileChooser = FileChooser()
                        fileChooser.title = "Open Resource File"
                        val file = fileChooser.showOpenDialog(WindowsAfk.pStage)
                        if (file != null) {
                            val mapper = ObjectMapper()
                            Map_Syp.loadMap(mapper.readValue(file, Map_Syp::class.java))
                        }
                    }
                })
            })

        }
        children.add(leftBar)
    }
}

