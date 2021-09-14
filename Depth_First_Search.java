package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Depth_First_Search <V extends Vertex, E extends Edge<V>>{
	
	/***************************************************************
	 * Globale Elemente
	 * 
	 * Variablen:
	 * @graph: der untersuchte Graph
	 * @pre_vertex: zum speichern des pred_Vertex
	 * @counter: zähler für First/Last bei der Suche
	 * @isTopologisch: True/False ob der Graph topologisch ist
	 * @Vertexobjekt: Ein Knotenobjekt um alle wichtigen Information
	 * (First, Last, Pred) zu speichern
	 * @comparator_id: sortiert eine Liste nach der ID aufsteigend
	 ***************************************************************/
	private Graph<V, E> graph;
	private V pre_vertex = null;
	private int counter = 0;
	private boolean isTopologisch = true;
	private Vertexobjekt objekt;
	private SortId<V> comparator_id = new SortId<V>();
	
	/***************************************************************
	 * Globale Elemente
	 * 
	 * Listen/Mengen:
	 * @visited: Sammlung von allen Knotenobjekten die besucht wurden
	 * @stack: Stack_system welches bei der DFS angwendet wird
	 * @vertices: Sammlung von allen vorhanden Knoten im Graph
	 * @neighbours: Sammlung von Knoten-nachbarn von einem Knoten
	 * (Bonus-Information):
	 * @stack_flows + @flow: zum speichern des Stackverlaufs 
	 ***************************************************************/
	Map<V, Vertexobjekt> visited = new HashMap();
	LinkedList<V> stack = new LinkedList<V>();
	List<V> vertices;
	LinkedList<V> neighbours;
	//Bonus
	List<LinkedList<V>> stack_flows = new ArrayList<LinkedList<V>>();
	LinkedList<V> flow;
	
	/***************************************************************
	 * Konstruktor
	 * 
	 * @para: Graph<V, E> graph
	 * Funktion: 
	 * 		- speichert alle vorhanden Knoten
	 * 		- speichert den momentanten Stackverlauf
	 * 		- der Stack wird für das Suchverfahren überprüft
	 ***************************************************************/
	public Depth_First_Search (Graph<V,E> graph) {
		this.graph = graph;
		vertices = new ArrayList<V>(graph.getVertices());
		stack_flows.add(stack);
		check_stack();
	}

	/***************************************************************
	 * check_stack - Methode
	 * 
	 * @para: -
	 * Funktion: 
	 * 		- Die Methode überprüft den derzeitigen Stand des Stacks
	 * 		- Sollte der Stack leer sein, wird mit einer Methode nach
	 * 		  unbesuchten Knoten gesucht 		  
	 *		- Andernfalls wird nach Stack-reihenfolge auf (Nachbarn oder
	 *		  Endstation) des Knoten überprüft
	 * Return:
	 *		  -
	 ***************************************************************/
	private void check_stack() {
		if(stack.isEmpty()) {
			check_unvisited_vertices();
		} else {
			V vertex = stack.getLast();
			//Hat der Knoten keine Nachbarn -> Aktualisieren und Löschen
			if (graph.getNeighbours(vertex) == null || graph.getNeighbours(vertex).isEmpty()) {
				lift_and_delete(vertex);
			} else {
				//Hat der Knoten Nachbarn -> überprüft ob schon-besucht oder nicht
				neighbours = new LinkedList<V>(graph.getNeighbours(vertex));
				check_neighbours(vertex);
			}
		}
	}
	
	/***************************************************************
	 * dive_and_visit - Methode
	 * 
	 * @para: V vertex
	 * Funktion: 
	 * 		- Der eingefügte Knoten wird dem stack dazugegeben.
	 * 		- Der Knoten initialisiert ein Knotenobjekt.
	 * 		- Das Knotenobjekt speichert die (First, Pred) Information,
	 * 		  und wird in die visited_Map eingefügt 
	 * 		- Der Stack wird gespeichert und wieder gecheckt.
	 * Return:
	 * 		-
	 ***************************************************************/
	private void dive_and_visit(V vertex) {	
			counter++;
			stack.add(vertex);
			objekt = new Vertexobjekt(vertex);
			objekt.setFirst(counter);
			objekt.setPred(pre_vertex);
			visited.put(vertex, objekt);				
			pre_vertex = vertex;
			
			flow = new LinkedList<V>(stack);
			stack_flows.add(flow);
			check_stack();
	}
	
	/***************************************************************
	 * lift_and_delete - Methode
	 * 
	 * @para: V vertex
	 * Funktion: 
	 * 		- Der eingefügte Knoten gelangt nur in diese Methode,
	 * 		  wenn er keine Nachbarn besitzt, oder die Nachbarn schon
	 * 		  alle besucht wurden.
	 *  	- (last) wird für das Knotenobjekt aktualisiert, 
	 *  	  sowie vom Stack und dem Graph gelöscht
	 * 		- Der Stack wird gespeichert und wieder gecheckt.
	 * Return:
	 * 		-
	 ***************************************************************/
	private void lift_and_delete(V vertex) {
		counter++;
		objekt = visited.get(vertex);
		objekt.setLast(counter);
		stack.removeLast();
		graph.removeVertex(vertex);
		if(stack.size() > 1) {
			pre_vertex = stack.getLast();
		} else {
			pre_vertex = null;
		}
		
		flow = new LinkedList<V>(stack);
		stack_flows.add(flow);
		check_stack();
	}

	/***************************************************************
	 * check_neighbours - Methode
	 * 
	 * @para: V vertex
	 * Funktion: 
	 * 		- überprüft die Nachbarn des eingefügten Knoten
	 * 		- Falls der Nachbar noch nicht im Stack ist, wird dieser Knoten
	 * 		  besucht
	 * 		- Andernfalls haben wir ein Nachbar der schon im Stack ist = Schleife
	 * 		  Dieser Nachbar_Knoten wird entfernt und wird auf andere Nachbarn
	 * 		  überprüft
	 * 		- Am ende wird der Stack gecheckt, wenn keine weiteren Nachbarn da sind 
	 * 		  zur Überprüfung.
	 * Return:
	 * 		-
	 ***************************************************************/
	private void check_neighbours(V vertex) {
		Collections.sort(neighbours, comparator_id);
		V nachbar = neighbours.getFirst();
		neighbours.removeFirst();
		if(!stack.contains(nachbar)) {
			dive_and_visit(nachbar);
		} else {
			isTopologisch = false;
			graph.removeVertex(nachbar);
			check_neighbours(vertex);
		}
		check_stack();
	}

	/***************************************************************
	 * check_unvisited_vertices - Methode
	 * 
	 * @para: -
	 * Funktion: 
	 * 		- Die Knotenliste wird durchlaufen, und dabei geschaut 
	 * 		  ob ein Knoten noch garnicht besucht wurde. Dieser wird
	 * 		  dann besucht.
	 * Return:
	 * 		-
	 ***************************************************************/
	private void check_unvisited_vertices() {
		List<V> knoten = new ArrayList<V>(vertices);
		Collections.sort(knoten, comparator_id);
		
		for (V vertex : vertices) {
			if(!visited.containsKey(vertex)) {
				dive_and_visit(vertex);
				break;
			}
		}	
	}
	
	/***************************************************************
	 * print_result - Methode
	 * 
	 * @para: -
	 * Funktion: 
	 * 		- Druckt das DFS-Ergebnis aus
	 * Return:
	 * 		-
	 ***************************************************************/
	public void print_result() {
		List<Vertexobjekt> objekt_liste = new ArrayList<Vertexobjekt>();
		
		List<V> keyset = new ArrayList<V>(visited.keySet());
		Collections.sort(keyset, comparator_id);
		for(V vertex : keyset) {
			objekt_liste.add(visited.get(vertex));
			System.out.println(visited.get(vertex).toString());
		}
		 
		if(isTopologisch) {
			Collections.sort(objekt_liste);
			System.out.print("Topologische Reihenfolge: [");
			for (Vertexobjekt objekt: objekt_liste) {
				System.out.print(objekt.getVertex() + " -> ");
			}
			System.out.print("Ende]");
			System.out.println(" ");
		} else {
			System.out.println("Der Graph kann nicht topoligsch geordnet werden");
		}
	}
	
	/***************************************************************
	 * print_stackflow - Methode
	 * 
	 * @para: -
	 * Funktion: 
	 * 		- Druckt den Stackverlauf der Suche aus
	 * Return:
	 * 		-
	 ***************************************************************/
	public void print_stackflow() {
		System.out.println("Stackflow vom Graphen");
		for (LinkedList<V> stack : stack_flows) {
			System.out.println(stack);
		}
	}
	
	/***************************************************************
	 * Innere Klasse
	 * 
	 * @para: V vertex
	 * Funktion: 
	 * 		- Erstellt ein Knotenobjekt um alle wichtigen Information
	 * 		  zu speichern
	 * Return:
	 * 		-
	 ***************************************************************/
	private class Vertexobjekt implements Comparable<Vertexobjekt>{
		
		//Variablen
		private int first;
		private int last;
		private V pred;
		private V vertex;		
		
		//Konstruktor
		public Vertexobjekt(V v) {
			this.vertex = v;
		}

		//Getters und Setters
		public void setFirst(int a) {
			this.first = a;
		}
		
		public void setLast(int a) {
			this.last = a;
		}
		
		public void setPred(V v) {
			this.pred = v;
		}

		public V getVertex() {
			return vertex;
		}
	
		@Override
		public String toString() {
			String s = "Knoten: " + vertex + ", First: "+ first + 
					", Last: " + last + ", Pred: " + pred;
			return s;
		}
		//Comparable zum sortieren nach dem Last-wert (topologische reihenfolge)
		@Override
		public int compareTo(Depth_First_Search<V, E>.Vertexobjekt objekt) {
			if (this.last > objekt.last) {
				return -1;
			} else if (this.last < objekt.last) {
				return 1;
			} else {
				return 0;
			}

		}
	}
}
