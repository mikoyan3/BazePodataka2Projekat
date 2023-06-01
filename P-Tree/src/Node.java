import java.util.List;
import java.util.ArrayList;

public class Node {
	private boolean isLeaf;
	private List<Region> regioni;
	private Region roditeljRegion;
	private Node roditeljNode;
	
	public Node(boolean isLeaf, Region roditelj, Node roditeljNode) {
		this.isLeaf = isLeaf;
		regioni = new ArrayList<>();
		this.roditeljRegion = roditelj;
		this.roditeljNode = roditeljNode;
	}
	
	public boolean isLeaf() {
		return isLeaf;
	}
	
	public void setRoditeljRegion(Region reg) {
		this.roditeljRegion = reg;
	}
	
	public void setRoditeljNode(Node node) {
		this.roditeljNode = node;
	}
	
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public List<Region> getRegioni(){
		return regioni;
	}
	
	public Region getRoditeljRegion() {
		return roditeljRegion;
	}
	
	public Node getRoditeljNode() {
		return roditeljNode;
	}
	
	//public List<Region> contains(Region region){
	//	List<Region> rezultat = new ArrayList<>();
	//	for(int i = 0; i < regioni.size(); i++) {
	//		if(regioni.get(i).intersects(region)) rezultat.add(regioni.get(i));
	//	}
	//	if(rezultat.size() > 0) return rezultat;
	//	else return null;
	//}
}
