package com.gluonapplication;

public class Arret {
	int dir;
	Noeud target;
	double poids;
	public Arret(Noeud n,double p) {
		target = n;	
		poids = p;
	}

}
