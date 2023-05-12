import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StackMazeSolver {
    private final char[][] maze;

    // Java 14+ (compiled with 17)
    record Vec2<A, B>(A x, B y) {
    }

    public static char[][] fromLines(String... lines) {
        char[][] maze = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            maze[i] = lines[i].toCharArray();
        }
        return maze;
    }

    public StackMazeSolver(char[][] maze) {
        this.maze = maze;
    }

    private Vec2<Integer, Integer> getStartPos(char[][] map) {
        // locate the start position
        for (int y = 0; y < map.length; y++) {
            char[] row = map[y];
            for (int x = 0; x < row.length; x++) {
                if (row[x] == 's') {
                    return new Vec2<>(x, y);
                }
            }
        }
        throw new RuntimeException("No start position found");
    }

    private char get(Vec2<Integer, Integer> pos) {
        return maze[pos.y()][pos.x()];
    }

    final Stack<Vec2<Integer, Integer>> front = new Stack<>();
    final Stack<Vec2<Integer, Integer>> path = new Stack<>();
    final Set<Vec2<Integer, Integer>> visited = new HashSet<>();

    public List<Vec2<Integer, Integer>> getNeighbors(Vec2<Integer, Integer> pos) {
        int width = maze[0].length;
        int height = maze.length;

        List<Vec2<Integer, Integer>> tentative = new ArrayList<>();
        List<Vec2<Integer, Integer>> results = new ArrayList<>();

        if (pos.x() > 0) tentative.add(new Vec2<>(pos.x() - 1, pos.y()));
        if (pos.x() < width) tentative.add(new Vec2<>(pos.x() + 1, pos.y()));
        if (pos.y() > 0) tentative.add(new Vec2<>(pos.x(), pos.y() - 1));
        if (pos.y() < height) tentative.add(new Vec2<>(pos.x(), pos.y() + 1));

        for (Vec2<Integer, Integer> neighbor : tentative) {
            if (visited.contains(neighbor)) continue;
            char at = get(neighbor);
            if (at == '#') {
                continue;
            }
            results.add(neighbor);
        }

        return results;
    }

    public Vec2<Integer, Integer> size() {
        return new Vec2<>(maze[0].length, maze.length);
    }

    public Stack<Vec2<Integer, Integer>> solve() {
        // In the case that the solve() method was called multiple times on the same instance
        // clear the stacks.
        front.clear();
        path.clear();
        visited.clear();

        Vec2<Integer, Integer> start = getStartPos(maze);
        front.push(start);
        while (front.size() > 0) {
            Vec2<Integer, Integer> current = front.pop();
            visited.add(current);
            path.push(current);
            char at = get(current);
            if (at == 'o') {
                // solution found!
                break;
            }
            List<Vec2<Integer, Integer>> neighbors = getNeighbors(current);
            if (neighbors.size() == 0) {
                // dead end, backtrack
                path.pop();
                continue;
            }
            front.pushAll(neighbors);
        }

        return path;
    }


    public static void main(String[] args) {
        StackMazeSolver sms = new StackMazeSolver(fromLines(
                "##########",
                "#        #",
                "# ###### #",
                "# #    # #",
                "# # # ## #",
                "# # #    #",
                "# # # ## #",
                "# # # ## #",
                "# # #    #",
                "# # ######",
                "# #      #",
                "# # ######",
                "#s#     o#",
                "##########"
        ));
        Stack<Vec2<Integer, Integer>> result = sms.solve();
        Stack<Vec2<Integer, Integer>> result2 = result.duplicate();

        // convert the result to a set
        Set<Vec2<Integer, Integer>> path = new HashSet<>();
        while (result.size() > 0) {
            path.add(result.pop());
        }
        // print the maze with the path
        Vec2<Integer, Integer> size = sms.size();
        for (int y = 0; y < size.y(); y++) {
            for (int x = 0; x < size.x(); x++) {
                Vec2<Integer, Integer> pos = new Vec2<>(x, y);
                char at = sms.get(pos);
                if (path.contains(pos) && at != 's' && at != 'o') {
                    System.out.print('*');
                } else {
                    System.out.print(at);
                }
            }
            System.out.println();
        }
        System.out.println(result2);
    }
}
