//@author Erika Noma, 2017-04-26, CS201
//This program is supposed to chooses two random articles and show the shortest path and its length between them,


import java.util.*;
import java.io.*;
import java.lang.Iterable;
import java.util.Iterator;
import java.net.URLDecoder;
import java.util.Map;

public class PathFinder{

  private Random vertex1;
  private Random vertex2;
  private boolean done;
  private UnweightedGraph graph;
  private Map<String, Integer> hm;
  private Map<Integer, String> hm_reversed;
  private Integer fv;
  private String firstVertex;
  private String nextNeighbor;
  private Deque<String> path;
  private Map<String, String> predecessors;
  private Map<String, Integer> length;

  //load files and choose random two articles as starting and ending

  public PathFinder(String vertexFile, String edgeFile) {
    File vFile = new File(vertexFile);
		File eFile = new File(edgeFile);

    graph = new CarlUnweightedGraph();
    hm = new HashMap<>();
    hm_reversed = new HashMap<>();

    Scanner articleData = null;
    Scanner linkData = null;

    try{
      articleData = new Scanner(vFile);
      linkData = new Scanner(eFile);

    }
    catch(FileNotFoundException e){
      System.out.println("Scanner error opening the file");
      System.out.println(e.getMessage());
      System.exit(1);
    }

      while(articleData.hasNextLine()){

          String Aline = articleData.nextLine();
          if (!Aline.startsWith("#") && !Aline.isEmpty()){
            try{
              String decoded_Aline = URLDecoder.decode(Aline, "UTF-8");
              Integer x = graph.addVertex();
              hm.put(decoded_Aline, x);
        }
        catch (UnsupportedEncodingException e){

        }
        }
      }

      while(linkData.hasNextLine()){
        String Lline = linkData.nextLine();
        if (!Lline.startsWith("#") && !Lline.isEmpty()){
          try{
            String[] lineSplit = Lline.split("\t");

            String e0 = lineSplit[0];
            String e1 = lineSplit[1];

            String decoded_e0 = URLDecoder.decode(e0,"UTF-8");
            String decoded_e1 = URLDecoder.decode(e1,"UTF-8");

            Integer v0 = hm.get(decoded_e0);
            Integer v1 = hm.get(decoded_e1);

            hm_reversed.put(v0, decoded_e0);
            hm_reversed.put(v1, decoded_e1);
            graph.addEdge(v0, v1);
          }
          catch (UnsupportedEncodingException e){
          }
        }
      }
    }


  public String getRandom(){
    Random r = new Random();
    int v = r.nextInt(graph.numVerts()) + 1;
    String article = hm_reversed.get(v);
    return article;
  }

//Pheudocode implemented from textbook
public int getShortestPathLength(String vertex1, String vertex2){


    Map<String, Boolean> visited = new HashMap<String, Boolean>();//list
    Deque<String> queue = new ArrayDeque<String>();
    Deque<String> path = new ArrayDeque<String>();
    Map<String, Integer> length = new HashMap<String, Integer>();
    Map<String, String> predecessors = new HashMap<String, String>();

    visited.put(vertex1, true);
    queue.add(vertex1);
    length.put(vertex1, 0);
    predecessors.put(vertex1, null);


    while (!queue.isEmpty()){
      String firstVertex = queue.removeFirst();
      Integer fv = hm.get(firstVertex);
      if (firstVertex == vertex2){
        break;
      }
       for (int i : graph.getNeighbors(fv)){
          //  Integer nn = neighbors.get(i);
           String nextNeighbor = hm_reversed.get(i);

           if (!visited.containsKey(nextNeighbor)){
               visited.put(nextNeighbor, true);
           }
          length.put(nextNeighbor, length.get(firstVertex)+1);
          predecessors.put(nextNeighbor, firstVertex);
          queue.addLast(nextNeighbor);
       }
   }

   path.push(vertex2);
   while (firstVertex != null && length.containsKey(firstVertex)){
     firstVertex = predecessors.get(firstVertex);
     path.push(firstVertex);
   }
   return length.get(vertex2);
 }

 public Iterable<String> getShortestPath(String vertex1, String vertex2) {
   List<String> path_list = new ArrayList<String>();
    while (firstVertex != null && length.containsKey(firstVertex)) {
  path_list.add(firstVertex);
             firstVertex = predecessors.get(firstVertex);
         }
    return path_list;
  }

  public static void main(String[] args){
    PathFinder pf = new PathFinder("articles.tsv", "links.tsv");
    String article1 = pf.getRandom();
    String article2 = pf.getRandom();
    System.out.println("Path from " + article1 + " to " + article2 + " , " + "length = " + pf.getShortestPathLength(article1, article2));
    System.out.println("path is =" + pf.getShortestPath(article1, article2));
  }
}
