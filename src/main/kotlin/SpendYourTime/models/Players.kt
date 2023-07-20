package SpendYourTime.models

class Players(val map : Map) : ArrayList<Player>() {
    fun updatePlayer(uuid: String, name: String, pos: Point, skin: Skin ): Player {
        val player = this.find { it.uuid == uuid }
        if (player != null) {
            if (player.pos != pos) {
                player.animationState = Player.Companion.AnimationState.WALKING
                if (player.pos.x < pos.x) {
                    player.direction = Player.Companion.Direction.LEFT
                } else if (player.pos.x > pos.x) {
                    player.direction = Player.Companion.Direction.RIGHT
                } else if (player.pos.y < pos.y) {
                    player.direction = Player.Companion.Direction.DOWN
                } else if (player.pos.y > pos.y) {
                    player.direction = Player.Companion.Direction.UP
                }
                player.pos = pos
            }

            player.name = name
            player.skin = skin
        } else {
            this.add(Player(uuid, name, pos, skin, map))
        }
        this.sortBy { it.pos.y }
        return this.find { it.uuid == uuid }!!
    }

    fun addPlayer( uuid: String, name: String, pos: Point, skin: Skin): Player {
        val player = this.find { it.uuid == uuid }
        if (player != null) {
            return player
        }
        this.add(Player(uuid, name, pos, skin, map))
        this.sortBy { it.pos.y }
        return this.find { it.uuid == uuid }!!
    }
}
