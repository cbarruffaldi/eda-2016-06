package grafos;

public class TestGraph {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Graph<Integer, MyArc> g = new Graph<Integer, MyArc>();

		assert g.isEmpty();
		
		g.AddVertex(1);
		
		assert ! g.isEmpty();
		
		g.AddVertex(2);
		g.AddVertex(3);
		g.AddVertex(4);
		g.AddVertex(5);
		g.AddVertex(6);
		g.AddVertex(7);
		g.AddVertex(8);
		
		g.addArc(1, 2, new MyArc(12));
		assert g.isBridge(1, 2);
		assert g.isBridge(2, 1);
		
		g.addArc(1, 6, new MyArc(16));
		g.addArc(2, 3, new MyArc(23));
		g.addArc(3, 4, new MyArc(34));
		g.addArc(3, 5, new MyArc(35));
		g.addArc(4, 5, new MyArc(45));
		g.addArc(6, 7, new MyArc(67));
		g.addArc(6, 8, new MyArc(68));
		
		assert g.isArc(1, 2).equals(12);
		assert g.isArc(1, 3).equals(13);
		assert g.isArc(2, 1).equals(12);
		assert g.isArc(3, 1).equals(13);
		assert g.isArc(6, 8).equals(68);
		assert g.isArc(8, 6).equals(683);
		assert g.isArc(1, 8) == null;
		assert g.isArc(11, 8) == null;
		
		assert g.arcCount() == 8;
		
		assert g.isBridge(1, 2);
		assert g.isBridge(2, 1);
		assert !g.isBridge(4, 5);
		assert !g.isBridge(5, 4);

		
		assert g.DFS(1).size() == 8;
		assert g.DFS(2).size() == 8;
		
		System.out.println("DFS desde 1:" + g.DFS(1));
		System.out.println("DFS desde 2:" + g.DFS(2));
		
		assert g.isConnected();
		assert g.connectedComponents()==1;
		assert g.cutVertex(1);
		assert g.cutVertex(2);
		assert g.cutVertex(3);
		assert g.cutVertex(6);
		assert !g.cutVertex(4);
		assert !g.cutVertex(5);
		assert !g.cutVertex(7);
		assert !g.cutVertex(8);
		
		g.AddVertex(9);
		assert g.connectedComponents()==2;
		
		g.AddVertex(10);
		assert g.connectedComponents()==3;
		
		assert g.cutVertex(1);
		assert g.cutVertex(2);
		assert g.cutVertex(3);
		assert g.cutVertex(6);
		assert !g.cutVertex(4);
		assert !g.cutVertex(5);
		assert !g.cutVertex(7);
		assert !g.cutVertex(8);
		assert !g.cutVertex(9);
		assert !g.cutVertex(10);
		
		g.addArc(9, 10, new MyArc(91));
		assert g.cutVertex(1);
		assert g.cutVertex(2);
		assert g.cutVertex(3);
		assert g.cutVertex(6);
		assert !g.cutVertex(4);
		assert !g.cutVertex(5);
		assert !g.cutVertex(7);
		assert !g.cutVertex(8);
		assert !g.cutVertex(9);
		assert !g.cutVertex(10);

		assert g.connectedComponents()==2;
		g.removeArc(10, 9);
		assert g.connectedComponents()==3;

		assert !g.isConnected();
		
		assert g.isPath(1,5);
		assert g.isPath(5,1);
		assert !g.isPath(9,1);
		assert !g.isPath(1,9);
		assert g.isPath(1,1);


		System.out.println("DFS 9: " + g.DFS(9));
		System.out.println("BFS 2:" + g.BFS(2));
		System.out.println("BFS 9:" + g.BFS(9));
		
//		g.addArc(5, 7, new MyArc(57));
//		System.out.println("DFS " + g.DFS(2));
//		System.out.println("BFS " + g.BFS(2));
//		System.out.println(g.isPath(1,5));
//		System.out.println(g.isPath(1,9));
//		System.out.println(g.isPath(1,1));
//		
//		// Testear camino minimo
//		Graph<Integer, MyArc> g2 = new Graph<Integer, MyArc>();
//		g2.AddVertex(1);
//		g2.AddVertex(2);
//		g2.AddVertex(3);
//		g2.AddVertex(4);
//		g2.AddVertex(5);
//		g2.addArc(1, 2, new MyArc(3));
//		g2.addArc(1, 3, new MyArc(1));
//		g2.addArc(3, 4, new MyArc(1));
//		g2.addArc(4, 5, new MyArc(3));
//		g2.addArc(2, 5, new MyArc(1));
//		System.out.println(g2.Dijkstra(1));
//		g2.addArc(5, 1, new MyArc(1));
//		System.out.println(g2.Dijkstra(1));
		
	}
}
