
package com.gluonapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

class Noeud implements Comparable<Noeud>{

    public String name;
    int i ;
    public Arret[] adjacents;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Noeud previous;
    public int prevN=0;
    public Noeud next;
    public double x ;
    public double y ;
    
    public Noeud(String argName){
        name = argName; 
        adjacents = new Arret[100];
        i=0;
    }
    public void add_arc(Noeud n , int poids){
    	i++;
    	adjacents[i] = new Arret(n, poids);
    	
    }
    public String toString() {
        return name; 
    }    
    public int compareTo(Noeud other){
        return Double.compare(minDistance, other.minDistance);
    }

}


public class Dijikstra {
    public static Noeud nIndex;
    public static boolean init=false;
    public static void computePaths(Noeud source)
    {
        source.minDistance = 0.;
        PriorityQueue<Noeud> noeudQueue = new PriorityQueue<Noeud>();
    noeudQueue.add(source);

    while (!noeudQueue.isEmpty()) {
        Noeud u = noeudQueue.poll();
        for (Arret e : u.adjacents)
            {
                if(e!=null){
                    Noeud v = e.target;
                    double poids = e.poids;
                    double distanceThroughU = u.minDistance + poids;
                    if (distanceThroughU < v.minDistance) {
                        noeudQueue.remove(v);
                        v.minDistance = distanceThroughU ;
                        v.previous = u;
                        v.prevN = (e.dir==0)?0:((e.dir==1||e.dir==2)?e.dir+2:e.dir-2);
                        noeudQueue.add(v);
                    }
                }
            }
        }
    }

    public static List<Noeud> getShortestPathTo(Noeud target)
    {
        List<Noeud> path = new ArrayList<Noeud>();
        
        for (Noeud noeud = target; noeud != null; noeud = noeud.previous)
            path.add(noeud);

        Collections.reverse(path);
        return path;
    }

    
}
