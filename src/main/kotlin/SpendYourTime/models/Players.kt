package SpendYourTime.models

class Players(val mapSyp : Map_Syp) : ArrayList<Player>() {

    fun updatePlayer(p : Player): Player {
        val player = this.find { it.uuid == p.uuid }
        if (player != null) {
            player.pos = p.pos
            player.skin = p.skin
            player.name = p.name
            player.direction = p.direction
            player.animationState = p.animationState
            player.score = p.score
            player.isOnline = p.isOnline
            return player
        }
        this.add(Player(p.uuid, p.name, p.pos, p.skin , p.score, p.isOnline))
        this.sortBy { it.score }
        return this.find { it.uuid == p.uuid }!!
    }
}
