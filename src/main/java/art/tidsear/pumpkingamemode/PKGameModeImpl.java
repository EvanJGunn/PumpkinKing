package art.tidsear.pumpkingamemode;

public class PKGameModeImpl implements PKGameMode{
    private PKState pkState;

    @Override
    public void Init() {
        System.out.println("PKG Init Func Called");
    }

    @Override
    public void Reset() {
        pkState = PKState.INIT;
    }

    @Override
    public void Update() {
        switch (pkState) {
            case INIT:
                break;
            case COUNTDOWN:
                break;
            case LOCKED_PUMPKIN:
                break;
            case UNLOCKED_PUMPKIN:
                break;
            case BOSS_BATTLE:
                break;
        }
    }
}
