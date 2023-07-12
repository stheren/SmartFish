package SpendYourTime.models

import kotlin.properties.Delegates

class Block {
    var x       by Delegates.notNull<Int>()
    var y       by Delegates.notNull<Int>()
    var color   by Delegates.notNull<Int>()
}
