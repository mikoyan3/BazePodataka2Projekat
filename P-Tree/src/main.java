import java.util.List;

public class main {

	public static void main(String[] args) {
		// Create an R-tree with M=4 and m=2
        RTree rTree = new RTree(4, 2);

        // Insert points
        Point point1 = new Point(1.0, 1.0);
        Point point2 = new Point(2.0, 2.0);
        Point point3 = new Point(3.0, 3.0);
        Point point4 = new Point(4.0, 4.0);
        Point point5 = new Point(5.0, 5.0);
        Point point6 = new Point(6.0, 6.0);

        rTree.insert(point1);
        rTree.insert(point2);
        rTree.insert(point3);
        rTree.insert(point4);
        rTree.insert(point5);
        rTree.insert(point6);
        rTree.printTree();
        
        // Perform a range search
        Point rangeTopLeft = new Point(2.0, 2.0);
        Point rangeBottomRight = new Point(5.0, 5.0);
        Region searchRegion = new Region(rangeTopLeft, rangeBottomRight, null);
        List<Region> results = rTree.rangeSearch(searchRegion);

        // Display the search results
        System.out.println("Range search results:");
        for (Region result : results) {
            System.out.println("Point: (" + result.getTopLeft().getX() + ", " + result.getTopLeft().getY() + ")");
        }
	}
}
