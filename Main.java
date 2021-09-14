package graph;

public class Main {
	
	public static void main(String [] args)	{
		
		Graph<Vertex, Edge<Vertex>> graph;
		Depth_First_Search<Vertex, Edge<Vertex>> dfs = null;
		
		System.out.println("----------------Graph8-------------------------");
		graph = GraphRead.FileToGraph("Beispiele/graph8.txt", true, true, false);
		dfs = new Depth_First_Search<Vertex, Edge<Vertex>>(graph);
		dfs.print_result();
		dfs.print_stackflow();
		
		System.out.println("-----------------Graph9------------------------");
		graph = GraphRead.FileToGraph("Beispiele/graph9.txt", true, true, false);
		dfs = new Depth_First_Search<Vertex, Edge<Vertex>>(graph);
		dfs.print_result();
		dfs.print_stackflow();
		
		System.out.println("------------------Graph20-----------------------");
		graph = GraphRead.FileToGraph("Beispiele/graph20.txt", true, true, false);
		dfs = new Depth_First_Search<Vertex, Edge<Vertex>>(graph);
		dfs.print_result();
		dfs.print_stackflow();

	}
}
