package SpendYourTime.models

class Skin() {
    var hair: Int = 0
    var outfit: Int = 0
    var body: Int = 0
    var accessory: Int = 0
    var eyes: Int = 0

    constructor(hair: Int, outfit: Int, body: Int, accessory: Int, eyes: Int) : this() {
        this.hair = hair
        this.outfit = outfit
        this.body = body
        this.accessory = accessory
        this.eyes = eyes
    }


}
