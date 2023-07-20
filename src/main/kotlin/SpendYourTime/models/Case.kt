package SpendYourTime.models

class Case() {
    var file: Int = 0
    var index: Int = 0

    constructor(file: Int, index: Int) : this() {
        this.file = file
        this.index = index
    }
}
