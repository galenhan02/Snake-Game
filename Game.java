import java.util.*;

public class Game {
    int width;
    int height;
    int points;
    Coordinate[][] board;
    

    public enum Space {
        EMPTY, SNAKE, APPLE
    }

    class Coordinate {
        int x;
        int y;
        Space space;
    
        public Coordinate(int x, int y, Space space) {
            this.x = x;
            this.y = y;
            this.space = space;
        }
    
        @Override
        public String toString() {
            switch(space) {
                case EMPTY:
                    return "-";
                case SNAKE:
                    return "o";
                case APPLE:
                    return "a";
                default:
                    return "?";
            }
        }
    
        @Override
        public boolean equals(Object o) {
            if(o == null || !(o instanceof Coordinate)) {
                return false;
            }
            Coordinate coor = (Coordinate) o;
            return this.x == coor.x && this.y == coor.y;
        }
    }

    public class Snake {
        Deque<Coordinate> bodyQueue;
        Coordinate head;
        Coordinate tail;
    
        public Snake(Coordinate coordinate) {
            bodyQueue = new ArrayDeque<>(); bodyQueue.addLast(coordinate);
            head = coordinate; tail = coordinate;
            coordinate.space = Space.SNAKE;
        }

        public void move(Coordinate coordinate) {
            if(head.equals(coordinate)) {
                return;
            }

            grow(coordinate);
            bodyQueue.removeLast();
            tail.space = Space.EMPTY; tail = bodyQueue.getLast();
        }

        public void grow(Coordinate coordinate) {
            head = coordinate;
            head.space = Space.SNAKE;
            bodyQueue.addFirst(coordinate);
        }
    }

    public Game(int width, int height, int points) {
        this.width = width;
        this.height = height;
        this.points = points;
        this.board = new Coordinate[height][width];
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {
                board[i][j] = new Coordinate(i, j, Space.EMPTY);
            }
        }
    }

    public void generateApple() {
        Random random = new Random();
        while(true) {
            Coordinate coordinate = board[random.nextInt(height)][random.nextInt(width)];
            if(coordinate.space != Space.SNAKE) {
                coordinate.space = Space.APPLE;
                break;
            }
        } 
    }

    public void render() {
        for(int i = 0; i < height; i++) {
            System.out.println("");
        }
        for(int i = -1; i < board.length + 1; i++) {
            for(int j = -1; j < board[0].length + 1; j++) {
               if(i == -1 || i == board.length) {
                    System.out.print("~");
                    if(j == board[0].length) {
                        System.out.println("");
                    }
                }
               else if(j == -1 || j == board[0].length) {
                    if(j == -1 || j == board[0].length) {
                        System.out.print("|");
                        if(j == board[0].length) {
                            System.out.println("");
                        }
                    }
               }
               else {  
                    System.out.print(board[i][j]);
               }
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Random random = new Random();
        String input = "";

        System.out.println("Use WASD for movement, q for quitting the game.\nPlease specify the following dimensions: [width] [height] (Example: 10 10)");
        String[] dimens; 
        while(true) {
            input = in.nextLine();
            dimens = input.split(" ");

            if(dimens.length != 2) {
                System.out.println("Invalid number of arguments, please try again");
            }
            break;
        }
        
        Game game = new Game(Integer.parseInt(dimens[0]), Integer.parseInt(dimens[1]), 0);
        Snake snake = game.new Snake(game.board[random.nextInt(game.height)][random.nextInt(game.width)]);
        game.generateApple();
        game.render();

        while(true) {
            input = in.next();
            if(input.equals("q")) {
                break;
            }

            Coordinate position;
            switch(input) {
                case "d":
                    position = game.board[snake.head.x][(snake.head.y + 1) % game.width];
                    break;
                case "s":
                    position = game.board[(snake.head.x + 1) % game.height][snake.head.y];
                    break;
                case "a":
                    position = game.board[snake.head.x][(snake.head.y - 1 + game.width) % game.width];
                    break;
                case "w":
                    position = game.board[(snake.head.x - 1 + game.height) % game.height][snake.head.y];
                    break;
                default:
                    position = game.board[snake.head.x][snake.head.y];
            }
            if(position.space == Space.APPLE) {
                snake.grow(position);
                game.generateApple();
                game.points++;
            }
            else if(!position.equals(game.board[snake.head.x][snake.head.y]) && position.space == Space.SNAKE){
                break;
            }
            else {
                snake.move(position);
            }
            
            game.render();
        }

        System.out.println("Game End!\nPoints: " + game.points);
        in.close();
    }

    
}
