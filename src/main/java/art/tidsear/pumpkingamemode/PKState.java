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

 */
public enum PKState {
    IDLE,
    COUNTDOWN,
    LOCKED_PUMPKIN,
    UNLOCKED_PUMPKIN,
}
