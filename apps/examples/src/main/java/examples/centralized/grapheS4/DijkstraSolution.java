/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package examples.centralized.grapheS4;

import java.util.HashMap;

import io.jbotsim.contrib.algos.Connectivity;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.MovementListener;
import io.jbotsim.core.event.SelectionListener;
import io.jbotsim.ui.JViewer;

/* Instructions du TD :
 * 1-Mettre a jour votre version de jbotsim (wget jbotsim.sf.net/jbotsim.jar)
 * La precedente n'a pas toutes les fonctionnalites requises
 * 2-Creez un nouveau projet utilisant cette version la
 * 3-Y inclure le present fichier. Il s'agira de remplir la methode 
 * computeDijkstraFrom() qui doit creer un arbre de plus court 
 * chemin a partir d'un noeud choisi 
 * (par exemple lien.setProperty("lineWidth",3)).
 * 
 * Etant donne un noeud v:
 * -la liste des voisins de v peut etre obtenue via v.getNeighbors().
 * -la distance avec un autre noeud u peut etre obtenue via v.distance(u).
 * 
 * N'hesitez pas a me poser d'autres questions sur l'API. 
 */
public class DijkstraSolution implements SelectionListener,MovementListener {
	Topology tp;
	HashMap<Node,Double> weights = new HashMap<Node,Double>();
	Node selectedNode;
	
	public DijkstraSolution(Topology tp){
		this.tp=tp;
		tp.addSelectionListener(this);
		tp.addMovementListener(this);
	}

	public void computeDijkstraFrom(Node source){
		
		if (!Connectivity.isConnected(tp))
			return;
			
		// Remet l'épaisseur des arêtes à 1
		for (Link l : tp.getLinks())
			l.setWidth(1);
		
		// Initialise le poids des sommets à +infini
		for (Node node : tp.getNodes())
			weights.put(node, Double.MAX_VALUE);
		
		// Poids 0 pour la source
		weights.put(source,0.0);
		
		while (!weights.isEmpty()){ // Tant qu'il reste des noeuds à examiner
			Node base = getGlobalMinimum(); // Récupère le noeud de poid min
			Double baseWeight = weights.get(base); // Récupère son poids
			for (Node neighbor : base.getNeighbors()){ // Pour chaque voisin
				if (weights.keySet().contains(neighbor)){ // Si le voisin n'est pas encore éliminé
					if (baseWeight+base.distance(neighbor)<weights.get(neighbor)){ // Si le poids peut être amélioré
						weights.put(neighbor,baseWeight+base.distance(neighbor)); // L'améliorer 
						neighbor.setProperty("parent", base); // Sélectionner la base courante comme parent
					}
				}
			}
			weights.remove(base); // Eliminer la base courante
		}
		
		for (Node node : tp.getNodes()){
			Node parent = (Node)node.getProperty("parent");
			if (parent!=null)
				node.getCommonLinkWith(parent).setWidth(5);
		}
	}
	
	private Node getGlobalMinimum(){
		Double min = Double.MAX_VALUE;
		Node globalMinimum = null;
		for (Node node : weights.keySet()){
			if (weights.get(node)<min){
				min=weights.get(node);
				globalMinimum=node;
			}
		}
		return globalMinimum;
	}
	
	public static void main(String args[]){
		Topology tp = new Topology();
		new DijkstraSolution(tp);
		new JViewer(tp);
	}

	@Override
	public void onMovement(Node arg0) {
		computeDijkstraFrom(selectedNode);
	}

	@Override
	public void onSelection(Node node) {
		System.out.println("tete");
		selectedNode = node;
		computeDijkstraFrom(selectedNode);
	}
}