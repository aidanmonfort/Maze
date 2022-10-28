package com.monfort;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Location{
    int row, col;

    public Location(int row, int col){
        this.row = row;
        this.col = col;
    }


    @Override
    public String toString() {
        return "[ " + row + ", " + col + " ]";
    }
}


public class Maze {
    private boolean[][] maze;

    private static Stack<Location> endPath;
    private int rows, cols;


    float SCREEN_WIDTH, SCREEN_HEIGHT;

    public Maze(String file, float height, float width) throws FileNotFoundException{
        genMaze(file);
        this.SCREEN_HEIGHT = height;
        this.SCREEN_WIDTH = width;
    }

    public void genMaze(String file) throws FileNotFoundException {
        File f = new File(file);
        Scanner sc = new Scanner(f);
        ArrayList<String> lines = new ArrayList<>();
        while(sc.hasNextLine()){
            lines.add(sc.nextLine());
        }
        int rows = lines.size();
        int cols = lines.get(0).length();
        this.rows = rows;
        this.cols = cols;
        maze = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = (lines.get(i).charAt(j) != '-');
            }
        }
        printMaze();
    }

    public void draw(){
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(140,150, 140, 1);
        shapeRenderer.rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        shapeRenderer.setColor(0, 0, 0, 1);
        float boxWidth = SCREEN_WIDTH/cols;
        float boxHeight = SCREEN_HEIGHT/rows;
        for(int i = 0; i < maze.length; i++){
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j]){
                    shapeRenderer.rect((j*boxWidth), SCREEN_HEIGHT -27 -(i*boxHeight), boxWidth, boxHeight);
                }
            }
        }
        shapeRenderer.end();
    }


    public boolean arrCont(ArrayList<Location> list, Location loc){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).row == loc.row && list.get(i).col == loc.col){
                return true;
            }
        }
        return false;
    }

    private ArrayList<Location> removeDouble(ArrayList<Location> one, ArrayList<Location> two){
        ArrayList<Location> ans = new ArrayList<>();
        for (int i = 0; i < one.size(); i++) {
            if (!arrCont(two, one.get(i))){
                ans.add(one.get(i));
            }
        }
        return ans;
    }

    private Stack<Location> copyStack(Stack<Location> old){
        Stack<Location> n = new Stack<>();
        for (int i = 0; i < old.size(); i++) {
            n.push(old.get(i));
        }
        return n;
    }



    public ArrayList<Stack<Location>> findPaths(){
        Stack<Location> path;
        ArrayList<Stack<Location>> allPaths = new ArrayList<>();
        Queue<Stack<Location>> possiblePaths;
        possiblePaths = new LinkedList<>();
        Stack<Location> first = new Stack<>();
        first.push(new Location(0,0));
        ArrayList<Location> visited = new ArrayList<>();
        visited.add(new Location(0,0));
        while(true){
            if(possiblePaths.isEmpty()){
                possiblePaths.offer(first);
            }
            path = possiblePaths.poll();
            allPaths.add(allPaths.size(), path);
            endPath = copyStack(path);
            if(path.peek().col == maze[0].length - 1 && path.peek().row == maze.length - 1){
                break;
            }
            Location curr = path.pop();
            ArrayList<Location> moves = neighbors(curr);
            moves = removeDouble(moves, visited);
            Location chosen;
            for (int i = 0; i < moves.size(); i++) {
                Stack<Location> nextPath = copyStack(path);
                chosen = moves.get(i);
                nextPath.push(curr);
                nextPath.push(chosen);
                visited.add(chosen);
                possiblePaths.offer(nextPath);
            }
        }
        return allPaths;
    }

    public boolean isValid(Location loc){
        return (loc.row >= 0 && loc.row < maze.length) && (loc.col >= 0 && loc.col < maze[0].length)
                && !maze[loc.row][loc.col];
    }

    public ArrayList<Location> neighbors(Location loc){
        ArrayList<Location> neigh = new ArrayList<>();
        if(isValid(new Location(loc.row - 1, loc.col))){
            neigh.add(new Location(loc.row - 1, loc.col));
        }
        if(isValid(new Location(loc.row + 1, loc.col))){
            neigh.add(new Location(loc.row + 1, loc.col));
        }
        if(isValid(new Location(loc.row, loc.col - 1))){
            neigh.add(new Location(loc.row, loc.col - 1));
        }
        if(isValid(new Location(loc.row, loc.col + 1))){
            neigh.add(new Location(loc.row, loc.col + 1));
        }
        return neigh;
    }

    public boolean validatePath(Stack<Location> path){
        Location curr;
        ArrayList<Location> allMoves = new ArrayList<>();
        if (path.isEmpty()){
            return false;
        }
        else{
            if (path.peek().row != maze.length || maze[0].length != path.peek().col){
                return false;
            }
            while (path.size() >= 2){
                curr = path.pop();
                Location prev = path.peek();
                ArrayList<Location> moves = neighbors(prev);
                if(!arrCont(moves, curr)){
                    return false;
                }
                if(arrCont(allMoves, curr)){
                    return false;
                }
                else{
                    allMoves.add(curr);
                }
            }
            return (path.peek().col != 0 || path.peek().row != 0);
        }
    }

    public void printMaze(){
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if(maze[i][j]){
                    System.out.print("[]");
                }
                else{
                    System.out.print("--");
                }
            }
            System.out.println();
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
