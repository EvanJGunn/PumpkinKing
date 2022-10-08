package art.tidsear.pumpkingamemode;

/*
- IDLE
Gamemode has either ended or not begun

- COUNTDOWN
From like 5 seconds, gives the players time to meme on each other,
while they all know their roles.
Disperse players about the map at the end, give them their objectives.
Give the PK their starting impostor items.
Enable PVP

- LOCKED_PUMPKIN
In the locked pumpkin state, players need to complete all of their tasks
in order to move on to the next state. In this state the Pumpkin King will be
constantly earning points that they can spend to gain items. Players earn points
by completing objectives, and by killing the pumpkin king (although this should be difficult to do).
Players will respawn with starting gear, and potentially randomized items.

- UNLOCKED_PUMPKIN
The door to the pumpkin kings area opens, and the core is exposed.
Some sort of redstone contraption needs to be used to trigger a command block that will
move the game onto the next state.

- BOSS_BATTLE
Respawn all the players at the correct positions with full HP and freeze point generation.
Give players time to buy in their respective shops. Move everyone to the boss arena.
Big fight ensues, and at some point the winner is declared, either when all players are dead,
or the PK is dead, or edge case where there is a trade between the last 2 (idk if even possible lol).
At this point all players are tpd to the starting area, PVP is disabled, and we remain in this state
until it is reset to INIT for a new game.

 */
public enum PKState {
    IDLE,
    COUNTDOWN,
    LOCKED_PUMPKIN,
    UNLOCKED_PUMPKIN,
    BOSS_BATTLE,
}
