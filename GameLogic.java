import java.util.*;

public class GameLogic implements PlayableLogic{
    Piece[][] pieces = new Piece[11][11]; // Game Board
    private boolean isSecondPlayerTurn = true; // player2 always play first
    private final Player player1;
    private final Player player2;
    private final King king;

    Stack<Position> undoPosition1 = new Stack<>();
    Stack<Position> undoPosition2 = new Stack<>();

    private final String[] keys1 = {"A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10",
            "A11", "A12", "A13", "A14", "A15", "A16", "A17", "A18",
            "A19", "A20", "A21", "A22", "A23", "A24"};
    private final String[] keys2 = {"D1", "D2", "D3", "D4", "D5", "D6", "K7", "D8", "D9", "D10",
            "D11", "D12", "D13"};


    //for part 2 Q1
    Map<String,ArrayList<Position>> player1Pieces = new HashMap<>();
    Map<String,ArrayList<Position>> player2Pieces = new HashMap<>();
    ///////////////////////////////////////////


    // part 2 Q2
    Map<String,Piece> player1kills = new HashMap<>();
    Map<String,Piece> player2kills = new HashMap<>();
    ///////////////////////////////////////////

    //part 3 Q3
    Map<String,Piece> player1moves = new HashMap<>();
    Map<String,Piece> player2moves = new HashMap<>();
    /////////////////////////////////////////////////

    //part 4 Q4
    Map<Position,ArrayList<Piece>> cells = new HashMap<>();
    /////////////////////////////////////////////////

    public GameLogic(){
        this.player1 = new ConcretePlayer(true);
        this.player2 = new ConcretePlayer(false);
        this.king = new King(player1);
        initBoard();
        initHashMapForPlayer1();
        initHashMapForPlayer2();
        initCells();
    }

     /*
       black pieces position
       at (3-4-5-6-7,0) (5,1)
       at (3-4-5-6-7,10) (5,9)
       at (0,3) (10,3)
       at (0,4) (10,4)
       at (0,5) (10,5)
       at (0,6) (10,6)
       at (0,7) (10,7)
       at (1,5) (9,5)
       -------------------------
       white pieces position
                  (5,3)
             (4,4)(5,4)(6,4)
        (3,5)(4,5)(5,5)(6,5)(7,5)    king at (5,5)
             (4,6)(5,6)(6,6)
                  (5,7)
     */

    /*
     initializing the pieces board and the moves for every single piece also the kills
     */
    private void initBoard() {
        int[][] player2PawnPosition = {
                {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, {5, 1}, {0, 3}, {10, 3},
                {0, 4}, {10, 4}, {0, 5}, {1, 5}, {9, 5}, {10, 5}, {0, 6}, {10, 6},
                {0, 7}, {10, 7}, {5, 9}, {3, 10}, {4, 10}, {5, 10}, {6, 10}, {7, 10}
        };

        int[][] player1PawnPosition = {
                {3,5}, {4, 4}, {4,5}, {4,6}, {5,3}, {5,4}, {5, 5},
                {5,6}, {5,7}, {6,4}, {6,5}, {6, 6}, {7,5}
        };

        int i = 0, j = 0;
        for (int[] pos : player1PawnPosition) {
            pieces[pos[1]][pos[0]] = new Pawn(player1);
            while(i< keys2.length){
                player1kills.put(keys2[i],pieces[pos[1]][pos[0]]);
                player1moves.put(keys2[i],pieces[pos[1]][pos[0]]);
                if(pos[1] == 5 && pos[0] == 5){
                    pieces[pos[1]][pos[0]] = king;
                    player1moves.put(keys2[i],pieces[pos[1]][pos[0]]);
                }
                i++;
                break;
            }
        }

        for (int[] pos : player2PawnPosition) {
            pieces[pos[0]][pos[1]] = new Pawn(player2);
            while (j < keys1.length){
                player2kills.put(keys1[j],pieces[pos[0]][pos[1]]);
                player2moves.put(keys1[j],pieces[pos[0]][pos[1]]);
                j++;
                break;
            }
        }

    }

    /*
    in the two hash maps below , we match the ID's according to the starting positions
    and every ID got position array list to store the positions
     */
    private void initHashMapForPlayer2() {
        int[][] positions = {
                {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, {5, 1}, {0, 3}, {10, 3},
                {0, 4}, {10, 4}, {0, 5}, {1, 5}, {9, 5}, {10, 5}, {0, 6}, {10, 6},
                {0, 7}, {10, 7}, {5, 9}, {3, 10}, {4, 10}, {5, 10}, {6, 10}, {7, 10}
        };

        for (int i = 0; i < keys1.length; i++) {
            String key = keys1[i];
            int x = positions[i][0];
            int y = positions[i][1];

            ArrayList<Position> positionsList = new ArrayList<>();
            positionsList.add(new Position(x, y));
            player2Pieces.put(key, positionsList);

        }
    }
    private void initHashMapForPlayer1(){
        int[][] positions = {
                {5,3},{4,4},{5,4},{6,4},{3,5},{4,5},{5,5},
                {6,5},{7,5},{4,6},{5,6},{6,6},{5,7}
        };

        for (int i = 0; i < keys2.length; i++) {
            String key = keys2[i];
            int x = positions[i][0];
            int y = positions[i][1];

            ArrayList<Position> positionsList = new ArrayList<>();
            positionsList.add(new Position(x, y));
            player1Pieces.put(key, positionsList);
        }
    }

    /*
    according to part2 Q4 :- we need to count how much pieces passed on specific cell on the map,
    according to this we initialize the map with positions and empty array list of pieces
    but if any cell already contain a piece we add it to array list
     */
    private void initCells() {
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                ArrayList<Piece> Pieces = new ArrayList<>();
                Position curr = new Position(x,y);
                if(getPieceAtPosition(curr) != null){
                    Pieces.add(pieces[curr.getX()][curr.getY()]);
                }
                cells.put(curr, Pieces);
            }
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////



    //PART 1
    //GAME LOGIC:-

    //1) INVALID move if the a and b are the same, also regular pawn can't go to the green corners
    private boolean invalidMove(Position a, Position b){
        boolean ans = (a.getX() == b.getX() && a.getY() == b.getY()) ||
                pieces[b.getX()][b.getY()] != null || !(a.getX() == b.getX() || a.getY() == b.getY());

        if(pieces[a.getX()][a.getY()] != king  &&
                ((b.getX() == 0 && b.getY() == 0) ||
                        (b.getX() == 0 && b.getY() == 10) ||
                        (b.getX() == 10 && b.getY() == 0) ||
                        (b.getX() == 10 && b.getY() == 10)) ){ans = true;}
        return ans;
    }

    //2) OBSTACLE FOUND if there is a piece in the way between A and B
    private Boolean obstacleFound(Position a, Position b){
        if(a.getY() == b.getY()) {
            if(a.getX() < b.getX()){
                for (int i = a.getX() + 1; i < b.getX(); i++) {
                    if (pieces[i][a.getY()] != null) {
                        return true;
                    }
                }
            }
            else {
                for (int i = a.getX() - 1; i > b.getX(); i--) {
                    if (pieces[i][a.getY()] != null){
                        return true;
                    }
                }
            }
        }
        else {
            if(a.getY() < b.getY()) {
                for (int i = a.getY() + 1; i < b.getY(); i++) {
                    if (pieces[a.getX()][i] != null) {
                        return true;
                    }
                }
            }
            else {
                for (int i = a.getY() - 1; i > b.getY(); i--) {
                    if (pieces[a.getX()][i] != null) {
                        return true;
                    }

                }
            }
        }
        return false;
    }


    //3) we check in the two functions below if the king got surrounded by 4 player2 pawns also
    // the king got surrounded by 3 player2 pawns and 4'th one is the board bounds
    // if the king is dead,we add to player2 a win ,then print the Statistics of the games
    private boolean isKingDead(){
        Piece [] kingNeighbour = Neighbour(king);
        int cnt = 0;
        for (int i = 0; i < 4; i++) {
            if(kingNeighbour[i] != null && kingNeighbour[i].getOwner() == player2){
                cnt++;
                if(cnt == 4){
                    player2.addWins();
                    printStatistics();
                    reset();
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isKingDead2(){
        boolean Dead = false;
        int cnt = 0;
        Piece [] kingNeighbour = Neighbour(king);
        Position kingPos = getPositionFromPiece(king);
        assert kingPos != null;
        if(kingPos.getY() == 0 || kingPos.getX() == 0 ||
                kingPos.getY() == 10 || kingPos.getX() == 10){
            for (int i = 0; i < 4 ; i++) {
                if(kingNeighbour[i] != null && kingNeighbour[i].getOwner() == player2){
                    // kingNeighbour[i].addKill();
                    cnt++;
                    if(cnt == 3){
                        Dead = true;
                        player2.addWins();
                        printStatistics();
                        reset();
                    }
                }
            }
        }
        return Dead;
    }


    //4)the function check if the king arrive to the corners,
    private boolean kingAtCorners(){
        if(pieces[0][0] == king ||
                pieces[0][10] == king ||
                pieces[10][0] == king ||
                pieces[10][10] == king){
            player1.addWins();
            printStatistics();
            reset();
            return true;
        }

        return false;
    }


    // also we need to check if the defender eats all the attacker pieces
    private boolean isAllBlackPiecesDead(){
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                if(pieces[x][y] != null && pieces[x][y].getOwner() == player2){
                    return false;
                }
            }
        }
        player1.addWins();
        printStatistics();
        reset();
        return true;
    }


    //5)
    /*
      we can kill the pawn , if we sandwiched from the left and right , or from above and button
       in kill2 function we take the b position as an argument , then we check if he has a 1-neighbour
       and his 1-neighbour not from his kind and not a king , we take the 2-neighbours of his 1-neighbour
        if 2-neighbor got two neighbours not from his kind we add kill to the piece that moved to "b"
        then we delete 2-neighbor from the map.
     */
    private void kill2(Position dist){
        Piece curr = getPieceAtPosition(dist);
        Piece[] neighbour = Neighbour(curr);
        for (int i = 0; i < 4; i++) {
            if(neighbour[i] != null && neighbour[i] != king && neighbour[i].getOwner() != curr.getOwner()){
                //Neighbour of Neighbour
                Piece NofN = neighbour[i];
                Piece[] nei = Neighbour(NofN);
                if(nei[0] != null && nei[2]!= null && nei[0]!= king && nei[2]!= king &&
                        nei[0].getOwner() != NofN.getOwner() && nei[2].getOwner() != NofN.getOwner()){
                    Position cur = getPositionFromPiece(NofN);
                    assert cur != null;
                    curr.addKill();
                    pieces[cur.getX()][cur.getY()] = null;
                    return;
                }

                if(nei[1] != null && nei[3]!= null && nei[1]!= king && nei[3]!= king &&
                        nei[1].getOwner() != NofN.getOwner()  && nei[3].getOwner() != NofN.getOwner()){
                    Position cur = getPositionFromPiece(NofN);
                    assert cur != null;
                    curr.addKill();
                    pieces[cur.getX()][cur.getY()] = null;
                    return;
                }
            }
        }
    }

    /*
    also we can kill a pawn if we trapped him between one of the boundaries and the opponent player
     */
    private void killPieceOnBounds(Piece p){
        Position pos = getPositionFromPiece(p);
        if(pos != null && pieces[pos.getX()][pos.getY()] != king){
            Piece[] neighbour = Neighbour(p);

            //capturing at first column
            if(neighbour[0] != king && neighbour[0] != null && pos.getX() == 1){
                if(pieces[pos.getX()][pos.getY()].getOwner() != neighbour[0].getOwner()){
                    Position curr = getPositionFromPiece(neighbour[0]);
                    assert curr != null;
                    pieces[pos.getX()][pos.getY()].addKill();
                    pieces[curr.getX()][curr.getY()] = null;
                }
            }
            //capturing at first row
            if(neighbour[1] != king && neighbour[1] != null && pos.getY() == 1){
                if(pieces[pos.getX()][pos.getY()].getOwner() != neighbour[1].getOwner()){
                    Position curr = getPositionFromPiece(neighbour[1]);
                    assert curr != null;
                    pieces[pos.getX()][pos.getY()].addKill();
                    pieces[curr.getX()][curr.getY()] = null;
                }
            }
            //capturing at last column
            if(neighbour[2] != king && neighbour[2] != null && pos.getX() == 9){
                if(pieces[pos.getX()][pos.getY()].getOwner() != neighbour[2].getOwner()){
                    Position curr = getPositionFromPiece(neighbour[2]);
                    assert curr != null;
                    pieces[pos.getX()][pos.getY()].addKill();
                    pieces[curr.getX()][curr.getY()] = null;
                }
            }
            //capturing at last row
            if(neighbour[3] != king && neighbour[3] != null && pos.getY() == 9){
                if(pieces[pos.getX()][pos.getY()].getOwner() != neighbour[3].getOwner()){
                    Position curr = getPositionFromPiece(neighbour[3]);
                    assert curr != null;
                    pieces[pos.getX()][pos.getY()].addKill();
                    pieces[curr.getX()][curr.getY()] = null;
                }
            }
        }
    }

    //this function calls the two above
    private void kill(Position dist){
        Piece curr = getPieceAtPosition(dist);
        killPieceOnBounds(curr);
        kill2(dist);
    }
    /*
            (1) aboveN
       (0) leftN     (2) rightN
           (3) buttonN
     */

    //returning the neighbour of any piece
    private Piece [] Neighbour(Piece p){
        Piece [] Neighbour  = new Piece[4];
        Position currP = getPositionFromPiece(p);
        if(currP != null) {
            if (currP.getX() > 0) {
                Neighbour[0] = getPieceAtPosition(new Position(currP.getX() - 1, currP.getY()));
            }
            if (currP.getY() > 0) {
                Neighbour[1] = getPieceAtPosition(new Position(currP.getX() , currP.getY() - 1));
            }
            if (currP.getX() < 10) {
                Neighbour[2] = getPieceAtPosition(new Position(currP.getX() + 1, currP.getY()));
            }
            if (currP.getY() < 10) {
                Neighbour[3] = getPieceAtPosition(new Position(currP.getX(), currP.getY() + 1));
            }
        }
        return Neighbour;
    }

    //returning the position of any piece
    private Position getPositionFromPiece(Piece p){
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if(pieces[i][j] != null){
                    if(pieces[i][j].equals(p)){return new Position(i,j);}
                }
            }
        }
        return null;
    }
    //////////////////////////////////////////////////////////////////////////////////////////



    //PART 2:


    /*
    related to Q1
    in the two functions bellow, we created a hash maps that contain <String , Array List<Position>>
    for player1 and player2, then we iterate on the  keys(D1,D2,D3 ....,A1,A2,A3.....) and pointing on the array list as a value
    of one of the keys
    then we check if this array contains "a" this means we found the piece that moved by checking the last index in the array
    then we add b to array
     */
    private void savePlayer1Moves(Position a,Position b){
        for (String key : keys2) {
            ArrayList<Position> arr = player1Pieces.get(key);
            if (a.equals(arr.get(arr.size()-1))) {
                arr.add(b);
            }
        }
    }
    private void savePlayer2Moves(Position a,Position b){
        for (String key : keys1) {
            ArrayList<Position> arr = player2Pieces.get(key);
            if (a.equals(arr.get(arr.size()-1))) {
                arr.add(b);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////


    //related to Q3
    //counts how much squares between a and b
    private int countTheMoves(Position a,Position b){
        int result = 0;
        if(a.getY() == b.getY()){
            result += Math.abs(a.getX()-b.getX());
        }
        else if(a.getX() == b.getX()){
            result += Math.abs(a.getY() - b.getY());
        }

        return result;
    }

    //related to Q4
    //
    private void checkCells(Position b){
        Piece curr = getPieceAtPosition(b);
        for (Map.Entry<Position,ArrayList<Piece>> entry: cells.entrySet()) {
            if (entry.getKey().equals(b) && !entry.getValue().contains(curr)) {
                ArrayList<Piece> currArr = entry.getValue();
                currArr.add(curr);
                cells.replace(entry.getKey(),currArr);
                break;
            }
        }
    }


    //in this comparator we take the ID'S(A1,A2...,D1,D2...)
    //we iterates on the keys (ID'S) then we saves only the numbers as a string
    //then we turn them to integers after this we compare between them to print them in ascending order
    Comparator<String> StringComp = (str1,str2) -> {
        StringBuilder n1 = new StringBuilder();
        StringBuilder n2 = new StringBuilder();

        for (char c : str1.toCharArray()) {
            if (Character.isDigit(c)) {
                n1.append(c);
            }
        }
        for (char c : str2.toCharArray()) {
            if (Character.isDigit(c)) {
                n2.append(c);
            }
        }
        int x = Integer.parseInt(n1.toString());
        int y = Integer.parseInt(n2.toString());
        return Integer.compare(x, y);

    };

    //prints the statistics of Q1
    private void print1(){
        Comparator<Map.Entry<String,ArrayList<Position>>> HashMapComp1 = (entry1,entry2) -> {
            //comparing between the arrays according to the size
            int result = Integer.compare(entry1.getValue().size(),entry2.getValue().size());

            if(result != 0){return result;}

            //if the sizes are equal we're comparing according to the ID'S using stringComp that we build above
            else{result = StringComp.compare(entry1.getKey(),entry2.getKey());}
            return result;
        };

        //saves player1 pieces and player2 pieces into a list
        List<Map.Entry<String, ArrayList<Position>>> sortedPlayer1Pieces =
                new ArrayList<>(player1Pieces.entrySet());

        List<Map.Entry<String, ArrayList<Position>>> sortedPlayer2Pieces =
                new ArrayList<>(player2Pieces.entrySet());

        //then we sort them according to the comparator
        sortedPlayer1Pieces.sort(HashMapComp1);
        sortedPlayer2Pieces.sort(HashMapComp1);

        //if player2 wins
        if(!isSecondPlayerTurn()){
            //we print player2 pieces
            for (Map.Entry<String, ArrayList<Position>> entry : sortedPlayer2Pieces) {
                if (entry.getValue().size() > 1) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
            //then we print player1 pieces
            for (Map.Entry<String, ArrayList<Position>> entry : sortedPlayer1Pieces) {
                if (entry.getValue().size() > 1) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
        }
        //else we print player1 pieces then player2 pieces
        else {
            for (Map.Entry<String, ArrayList<Position>> entry : sortedPlayer1Pieces) {
                if (entry.getValue().size() > 1) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
            for (Map.Entry<String, ArrayList<Position>> entry : sortedPlayer2Pieces) {
                if (entry.getValue().size() > 1) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
        }
    }

    //prints the statistics of Q2
    private void print2(){

        //saves player1 kills into list
        List<Map.Entry<String, Piece>> sortedPlayer1Kills
                = new ArrayList<>(player1kills.entrySet());
        //saves player2 kills into list
        List<Map.Entry<String, Piece>> sortedPlayer2Kills
                = new ArrayList<>(player2kills.entrySet());


        Comparator<Map.Entry<String, Piece>> Comp2 = (entry1, entry2) -> {
            //comparing between the kills , descending order
            int kills = Integer.compare(entry2.getValue().getKills(), entry1.getValue().getKills());

            if (kills != 0) {return kills;}
            //if the kills are equal we're comparing in ascending order using string comparator
            else{return StringComp.compare(entry1.getKey(),entry2.getKey());}
        };


        // here we're merging the two lists above
        List<Map.Entry<String, Piece>> mergedList = new ArrayList<>(sortedPlayer2Kills);
        mergedList.addAll(sortedPlayer1Kills);
        // then we sort the merged list
        mergedList.sort(Comp2);

        //deleting all the zero's kills
        mergedList.removeIf(entry -> entry.getValue().getKills() == 0);

        //if player1 wins
        if (isSecondPlayerTurn()) {
            //we check if the current key and the ones come after is the same value in mergedList
            //we check if the current key starting with"A" and the other one starting with "D"
            //then we need to swap them because player1 wins
            for (int i = 0; i < mergedList.size() - 1; i++) {
                int j = i + 1;
                if (mergedList.get(i).getValue().getKills() == mergedList.get(j).getValue().getKills() &&
                        mergedList.get(i).getKey().startsWith("A") && mergedList.get(j).getKey().startsWith("D")) {
                    Collections.swap(mergedList, i, j);
                }
            }
        }
        else{
            //we do the same thing here
            for (int i = 0; i < mergedList.size() - 1; i++) {
                int j = i + 1;
                if (mergedList.get(i).getValue().getKills() == mergedList.get(j).getValue().getKills() &&
                        mergedList.get(i).getKey().startsWith("D") && mergedList.get(j).getKey().startsWith("A")) {
                    Collections.swap(mergedList, i, j);
                }
            }
        }
        //after all that we print the kills for every piece
        for(Map.Entry<String, Piece> entry : mergedList) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getKills() + " kills");
        }

    }

    //prints the statistics of Q3
    //we did the same in print2
    private void print3(){
        List<Map.Entry<String,Piece>> sortedPlayer1Moves =
                new ArrayList<>(player1moves.entrySet());
        List<Map.Entry<String,Piece>> sortedPlayer2Moves =
                new ArrayList<>(player2moves.entrySet());

        Comparator<Map.Entry<String, Piece>> Comp2 = (entry1, entry2) -> {

            int moves = Integer.compare(entry2.getValue().getNumOfMoves(), entry1.getValue().getNumOfMoves());

            if (moves != 0) {
                return moves;
            }
            else{return StringComp.compare(entry1.getKey(),entry2.getKey());}
        };
        List<Map.Entry<String, Piece>> mergedList = new ArrayList<>(sortedPlayer2Moves);
        mergedList.addAll(sortedPlayer1Moves);
        mergedList.sort(Comp2);
        mergedList.removeIf(entry -> entry.getValue().getNumOfMoves() == 0);

        if (isSecondPlayerTurn()) {
            for (int i = 0; i < mergedList.size() - 1; i++) {
                int j = i + 1;
                if (mergedList.get(i).getValue().getNumOfMoves() == mergedList.get(j).getValue().getNumOfMoves() &&
                        mergedList.get(i).getKey().startsWith("A")
                        && (mergedList.get(j).getKey().startsWith("D") || mergedList.get(j).getKey().startsWith("K"))){
                    Collections.swap(mergedList, i, j);
                }
            }
        }
        else{
            for (int i = 0; i < mergedList.size() - 1; i++) {
                int j = i + 1;
                if (mergedList.get(i).getValue().getKills() == mergedList.get(j).getValue().getKills() &&
                        (mergedList.get(j).getKey().startsWith("D") || mergedList.get(j).getKey().startsWith("K"))
                        && mergedList.get(j).getKey().startsWith("A")) {
                    Collections.swap(mergedList, i, j);
                }
            }
        }
        for(Map.Entry<String, Piece> entry : mergedList) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getNumOfMoves() + " squares");
        }
    }

    //print the statistics of Q4
    private void print4(){

        //saves the cells and how much pawns passed on it into a list
        List<Map.Entry<Position,ArrayList<Piece>>> List = new ArrayList<>(cells.entrySet());

        //we need to print the cells that at least two pawns passed on it ,
        // because of that we don't need to print the cells that got 0 or 1 passed on it
        // then we deleted from the list
        List.removeIf(entry -> entry.getValue().size() == 0 || entry.getValue().size() == 1);

        Comparator<Map.Entry<Position,ArrayList<Piece>>> comp4 = (entry1,entry2) ->{

            //comparing in descending order between the sizes of the array lists
            int result = Integer.compare(entry2.getValue().size(),entry1.getValue().size());
            if(result != 0){return result;}

            //if the sizes are equal we need to sort according to the X's
            else if(entry1.getKey().getX() != entry2.getKey().getX()){
                int x1 = entry1.getKey().getX();
                int x2 = entry2.getKey().getX();
                result = Integer.compare(x1,x2);
            }
            //if the X's are equal we need to sort according to the Y's
            else{
                int y1 = entry1.getKey().getY();
                int y2 = entry2.getKey().getY();
                result = Integer.compare(y1,y2);
            }
            return result;
        };

        //we sort according to the comparator
        List.sort(comp4);

        //then we iterate on the List
        for (Map.Entry<Position,ArrayList<Piece>> entry: List ) {
            System.out.println(entry.getKey()+""+entry.getValue().size()+" pieces");
        }
    }


    //calling tho four functions above
    private void printStatistics(){
        print1();
        System.out.println("***************************************************************************");
        print2();
        System.out.println("***************************************************************************");
        print3();
        System.out.println("***************************************************************************");
        print4();
        System.out.println("***************************************************************************");

    }

    @Override
    public boolean move(Position a, Position b) {
        int moves = countTheMoves(a,b); //calculating the moves between a and b
        //returning false if invalid move or there is an obstacle
        if(invalidMove(a,b) || obstacleFound(a,b)){return false;}

        //player2 turn
        if(isSecondPlayerTurn && pieces[a.getX()][a.getY()].getOwner() == player2){


            savePlayer2Moves(a,b);

            //transferring the piece at position "a" to position "b"
            Piece curr = pieces[a.getX()][a.getY()];
            pieces[a.getX()][a.getY()] = null;
            pieces[b.getX()][b.getY()] = curr;
            //after transferring this piece we how much moves he did
            pieces[b.getX()][b.getY()].addMove(moves);

            undoPosition1.push(new Position(a.getX(),a.getY()));
            undoPosition2.push(new Position(b.getX(),b.getY()));

            // increasing the number of how many pieces passed on the cell(pieces[b.getX()][b.getY()]
            checkCells(b);
            //checking if the piece we moved killed other pieces
            kill(b);
            isSecondPlayerTurn = false;
            isGameFinished();
            return true;
        }

        //we do the same for player1 except that the king can't kill
        if(!isSecondPlayerTurn && pieces[a.getX()][a.getY()] == king){
            savePlayer1Moves(a,b);

            Piece curr = pieces[a.getX()][a.getY()];
            pieces[a.getX()][a.getY()] = null;
            pieces[b.getX()][b.getY()] = curr;
            pieces[b.getX()][b.getY()].addMove(moves);

            undoPosition1.push(new Position(a.getX(),a.getY()));
            undoPosition2.push(new Position(b.getX(),b.getY()));


            checkCells(b);
            isSecondPlayerTurn = true;
            isGameFinished();
            return true;
        }

        if(!isSecondPlayerTurn && pieces[a.getX()][a.getY()].getOwner() == player1){
            savePlayer1Moves(a,b);

            Piece curr = pieces[a.getX()][a.getY()];
            pieces[a.getX()][a.getY()] = null;
            pieces[b.getX()][b.getY()] = curr;
            pieces[b.getX()][b.getY()].addMove(moves);
            undoPosition1.push(new Position(a.getX(),a.getY()));
            undoPosition2.push(new Position(b.getX(),b.getY()));

            isSecondPlayerTurn = true;
            checkCells(b);
            kill(b);
            isGameFinished();
            return true;
        }


        return false;
    }
    @Override
    public Piece getPieceAtPosition(Position position) {
        return pieces[position.getX()][position.getY()];
    }
    @Override
    public Player getFirstPlayer() {
        return player1;
    }
    @Override
    public Player getSecondPlayer() {
        return player2;
    }

    @Override
    public boolean isGameFinished() {
        return isKingDead() || isKingDead2() || kingAtCorners() || isAllBlackPiecesDead();
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return isSecondPlayerTurn;
    }

    @Override
    public void reset() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                pieces[i][j] = null;
            }
        }
        //we have one instance of the king, after the game reset we need also to rest his moves
        int kingMoves = king.getNumOfMoves();
        king.addMove(-kingMoves);

        player1Pieces.clear();
        player2Pieces.clear();
        undoPosition1.clear();
        undoPosition2.clear();
        player1kills.clear();
        player2kills.clear();
        player1moves.clear();
        player2moves.clear();
        cells.clear();


        initBoard();
        initHashMapForPlayer1();
        initHashMapForPlayer2();
        initCells();

    }

    @Override
    public void undoLastMove() {
        if(!undoPosition1.empty() && !undoPosition2.empty()){
            if(isSecondPlayerTurn){
                isSecondPlayerTurn = false;
                Position pos1 = undoPosition2.pop();
                Piece curr = getPieceAtPosition(pos1);
                Position pos2 = undoPosition1.pop();

                pieces[pos1.getX()][pos1.getY()] = null;
                pieces[pos2.getX()][pos2.getY()] = curr;
            }
            else{
                isSecondPlayerTurn = true;
                Position pos1 = undoPosition2.pop();
                Piece curr = getPieceAtPosition(pos1);
                Position pos2 = undoPosition1.pop();

                pieces[pos1.getX()][pos1.getY()] = null;
                pieces[pos2.getX()][pos2.getY()] = curr;
            }
        }

    }

    @Override
    public int getBoardSize(){

        return 11;// the board size is 11x11
    }

}
