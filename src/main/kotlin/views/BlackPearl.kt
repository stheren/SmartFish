package views

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

class BlackPearl private constructor() : VBox() {
    companion object {
        val instance = BlackPearl()
    }

    init {
        // Chaînes de caractères à afficher
        val messages = listOf(
                "Dring, Dring ☎️",
                "- Oui allo ?",
                "- C'est bien la boutique de donnut sucré au sucre ??! 😲",
                "- Vous êtes bien renseigné",
                "- Quoi ! Vous voulez dire que Corentin dors dans les murs de Martin ?",
                "- Comment ça mon reuf !"
                             )

        // Créer et ajouter des Labels à la VBox pour chaque message
        messages.forEach { message ->
            val label = Label(message)
            label.textFill = Color.WHITE // Définir la couleur du texte en blanc
            label.font = Font.font("Arial", FontWeight.BOLD, 14.0) // Mettre le texte en gras
            label.alignment = Pos.BASELINE_LEFT // Aligner le texte à gauche
            this.children.add(label)
        }

        // Mise en forme de la VBox
        this.alignment = Pos.CENTER_LEFT // Aligner le contenu de la VBox à gauche
        this.padding = Insets(10.0)
        this.spacing = 10.0
        this.style = "-fx-background-color: black;" // Définir le fond en noir
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE)
        VBox.setVgrow(this, Priority.ALWAYS)
    }
}
