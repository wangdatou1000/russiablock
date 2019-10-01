package games.russiablock;

public class StartGameAction implements Command {
    private AutoGame computerGame;
    public StartGameAction(AutoGame computerGame){
        this.computerGame = computerGame;
    }

    @Override
    public Object action(Object obj) {
        computerGame.createnewblocks();
        computerGame.next = true;
        computerGame.xia.setSuspend(false);
        return new Enviroment(computerGame.gm,computerGame.getblk());
    }
}
