public class King extends ConcretePiece{
    private int moves = 0;
    public King(Player owner) {
        super(owner, "♔");
    }
    @Override
    public void addKill() {
        return;
    }

    @Override
    public int getKills() {
        return 0;
    }

    @Override
    public void addMove(int m) {
        this.moves += m;
    }

    @Override
    public int getNumOfMoves() {
        return moves;
    }

}
