public class Pawn extends ConcretePiece{
    private int kills = 0;
    private int moves;
    public Pawn(Player owner) {
        super(owner,isPlayerOne(owner));
    }

    private static String isPlayerOne(Player p){
        if (p.isPlayerOne()) {
            return "♙"; // unicode for ♙  white pawns
        }
        else {
            return "♟";// Unicode for ♟ black pawns
        }
    }
    @Override
    public int getKills() {
        return kills;
    }
    @Override
    public int getNumOfMoves() {
        return moves;
    }
    @Override
    public void addKill(){
        this.kills+=1;
    }
    @Override
    public void addMove(int m) {
        this.moves += m;
    }

}
