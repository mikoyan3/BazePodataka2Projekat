import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RTree {
	private int M;
	private int m;
	private Node root;
	
	public RTree(int M, int m) {
		this.M = M;
		this.m = m;
		root = new Node(true, null, null);
	}
	
	public List<Region> rangeSearch(Region region){
		List<Region> rezultat = new ArrayList<>();
		search(rezultat, region, root);
		return rezultat;
	}
	
	public void search(List<Region> rezultat, Region region, Node node) {
		if(node.isLeaf() == true) {
			for(int i = 0; i < node.getRegioni().size(); i++) {
				if(node.getRegioni().get(i).contains(region)) {
					rezultat.add(node.getRegioni().get(i));
				}
			}
		} else {
			List<Region> sadrzeIliPresecaju = new ArrayList<>();
			for(int i = 0; i < node.getRegioni().size(); i++) {
				if(node.getRegioni().get(i).contains(region) || node.getRegioni().get(i).intersects(region)) {
					sadrzeIliPresecaju.add(node.getRegioni().get(i));
				}
			}
			for(int i = 0; i < sadrzeIliPresecaju.size(); i++) {
				search(rezultat, region, sadrzeIliPresecaju.get(i).getPotomak());
			}
		}
	}
	
	public void insert(Point point) {
		Region region = new Region(point, point, null);
		Node leafNode = izaberiList(region);
		if(leafNode.getRegioni().size() < M) {
			leafNode.getRegioni().add(region);
			update(leafNode, region);
		} else {
			List<Node> splitNodes = splitList(leafNode, region);
			adjust(splitNodes.get(0), splitNodes.get(1));
		}
	}
	
	private Node izaberiList(Region region) {
		Node node = root;
		while(node.isLeaf() != true) {
			double minimum = Double.MAX_VALUE;
			double minimumPovrsina = Double.MAX_VALUE;
			Node selected = null;
			for(int i = 0; i < node.getRegioni().size(); i++) {
				double povecanje = izracunajPovecanje(node.getRegioni().get(i), region);
				if(povecanje < minimum) {
					minimum = povecanje;
					selected = node.getRegioni().get(i).getPotomak();
					minimumPovrsina = node.getRegioni().get(i).getArea();
				} else if(povecanje == minimum) {
					if(node.getRegioni().get(i).getArea() < minimumPovrsina) {
						selected = node.getRegioni().get(i).getPotomak();
						minimumPovrsina = node.getRegioni().get(i).getArea();
					}
				}
			}
			node = selected;
		}
		return node;
	}
	
	public void update(Node leafNode, Region region) {
		if(root != leafNode) {
			Node node = leafNode;
			while (node != null) {
				Region roditeljRegion = node.getRoditeljRegion();
				if(roditeljRegion != null) {
					roditeljRegion.updateMBR(region);
				}
				node = node.getRoditeljNode();
			}
		}
	}
	
	public Region[] izaberiSeeds(List<Region> regioni) {
		Region[] seeds = new Region[2];
		double maximum = Double.MIN_VALUE;
		
		for(int i = 0; i < regioni.size(); i++) {
			Region reg1 = regioni.get(i);
			
			for(int j = i + 1; j < regioni.size(); j++) {
				Region reg2 = regioni.get(j);
				
				double distance = reg1.distance(reg2);
				
				if(distance > maximum) {
					maximum = distance;
					seeds[0] = reg1;
					seeds[1] = reg2;
				}
			}
		}
		return seeds;
	}
	
	public double izracunajPovecanje(Region reg1, Region reg2) {
		double area1 = reg1.getArea();
		double area2 = reg2.getArea();
		double unionArea = reg1.izracunajUnionArea(reg2);
		return unionArea - area1 - area2;
	}
	
	private void adjust(Node splitNode1, Node splitNode2) {
		if(splitNode1 == root || splitNode2 == root) {
			root = napraviKoren(splitNode1, splitNode2);
		} else if (splitNode1.getRoditeljRegion() == null || splitNode2.getRoditeljRegion() == null){
			root = napraviKoren(splitNode1, splitNode2);
		} else {
			Node parentNode = splitNode1.getRoditeljNode();
	        Region parentRegion = splitNode1.getRoditeljRegion();
	        parentNode.getRegioni().remove(parentRegion);

	        Region reg1 = new Region(splitNode1.getRegioni(), splitNode1);
	        Region reg2 = new Region(splitNode2.getRegioni(), splitNode2);

	        parentNode.getRegioni().add(reg1);
	        parentNode.getRegioni().add(reg2);
	        
	        splitNode1.setRoditeljRegion(reg1);
	        splitNode2.setRoditeljRegion(reg2);
	        splitNode1.setRoditeljNode(parentNode);
	        splitNode2.setRoditeljNode(parentNode);
	        
	        if (parentNode.getRegioni().size() > M) {
	            List<Node> splitNodes = splitObican(parentNode);
	            adjust(splitNodes.get(0), splitNodes.get(1));
	        }
		}
	}
	
	private Node napraviKoren(Node node1, Node node2) {
		Node rootNode = new Node(false, null, null);
		Region reg1 = new Region(node1.getRegioni(), node1);
		Region reg2 = new Region(node2.getRegioni(), node2);
		node1.setRoditeljNode(rootNode);
		node1.setRoditeljRegion(reg1);
		node2.setRoditeljNode(rootNode);
		node2.setRoditeljRegion(reg2);
		rootNode.getRegioni().add(reg1);
		rootNode.getRegioni().add(reg2);
		return rootNode;
	}
	
	
	private List<Node> splitObican(Node node){
		List<Region> regioni = node.getRegioni();
		Region[] seeds = izaberiSeeds(regioni);
		Node newNode1 = new Node(false, node.getRoditeljRegion(), node.getRoditeljNode());
		Node newNode2 = new Node(false, node.getRoditeljRegion(), node.getRoditeljNode());
		newNode1.getRegioni().add(seeds[0]);
		newNode2.getRegioni().add(seeds[1]);
		regioni.remove(seeds[0]);
		regioni.remove(seeds[1]);
		
		while(!regioni.isEmpty()) {
			Region nextReg = regioni.get(0);
			Region region1 = new Region(newNode1.getRegioni(), null);
			Region region2 = new Region(newNode2.getRegioni(), null);
			double povecanje1 = izracunajPovecanje(region1, nextReg);
			double povecanje2 = izracunajPovecanje(region2, nextReg);
			
			if(povecanje1 < povecanje2) {
				newNode1.getRegioni().add(nextReg);
				regioni.remove(nextReg);
			} else if(povecanje2 < povecanje1) {
				newNode2.getRegioni().add(nextReg);
				regioni.remove(nextReg);
			} else {
				double area1 = region1.getArea();
				double area2 = region2.getArea();
				
				if(area1 < area2) {
					newNode1.getRegioni().add(nextReg);
					regioni.remove(nextReg);
				} else if (area2 < area1) {
					newNode2.getRegioni().add(nextReg);
					regioni.remove(nextReg);
				} else {
					if(newNode1.getRegioni().size() < newNode2.getRegioni().size()) {
						newNode1.getRegioni().add(nextReg);
						regioni.remove(nextReg);
					} else {
						newNode2.getRegioni().add(nextReg);
						regioni.remove(nextReg);
					}
				}
			}
		}
		for(int i = 0; i < newNode1.getRegioni().size(); i++) {
			newNode1.getRegioni().get(i).getPotomak().setRoditeljNode(newNode1);
			newNode1.getRegioni().get(i).getPotomak().setRoditeljRegion(newNode1.getRegioni().get(i));
		}
		
		for(int i = 0; i < newNode2.getRegioni().size(); i++) {
			newNode2.getRegioni().get(i).getPotomak().setRoditeljNode(newNode2);
			newNode2.getRegioni().get(i).getPotomak().setRoditeljRegion(newNode2.getRegioni().get(i));
		}
		
		List<Node> splitNodes = new ArrayList<>();
		splitNodes.add(newNode1);
		splitNodes.add(newNode2);
		
		return splitNodes;
		
	}
	
	public List<Node> splitList(Node leafNode, Region region){ //region pa node
		Node newLeaf1 = new Node(true, leafNode.getRoditeljRegion(), leafNode.getRoditeljNode());
		Node newLeaf2 = new Node(true, leafNode.getRoditeljRegion(), leafNode.getRoditeljNode());
		List<Region> regioni = new ArrayList<>(leafNode.getRegioni());
		regioni.add(region);
		Region[] seeds = izaberiSeeds(regioni);
		newLeaf1.getRegioni().add(seeds[0]);
		newLeaf2.getRegioni().add(seeds[1]);
		
		regioni.remove(seeds[0]);
		regioni.remove(seeds[1]);
		
		while(!regioni.isEmpty()) {
			Region nextReg = regioni.get(0);
			Region region1 = new Region(newLeaf1.getRegioni(), null);
			Region region2 = new Region(newLeaf2.getRegioni(), null);
			double povecanje1 = izracunajPovecanje(region1, nextReg);
			double povecanje2 = izracunajPovecanje(region2, nextReg);
			
			if(povecanje1 < povecanje2) {
				newLeaf1.getRegioni().add(nextReg);
				regioni.remove(nextReg);
			} else if(povecanje2 < povecanje1) {
				newLeaf2.getRegioni().add(nextReg);
				regioni.remove(nextReg);
			} else {
				double area1 = region1.getArea();
				double area2 = region2.getArea();
				
				if(area1 < area2) {
					newLeaf1.getRegioni().add(nextReg);
					regioni.remove(nextReg);
				} else if (area2 < area1) {
					newLeaf2.getRegioni().add(nextReg);
					regioni.remove(nextReg);
				} else {
					if(newLeaf1.getRegioni().size() < newLeaf2.getRegioni().size()) {
						newLeaf1.getRegioni().add(nextReg);
						regioni.remove(nextReg);
					} else {
						newLeaf2.getRegioni().add(nextReg);
						regioni.remove(nextReg);
					}
				}
			}
		}
		
		List<Node> splitNodes = new ArrayList<>();
		splitNodes.add(newLeaf1);
		splitNodes.add(newLeaf2);
		
		return splitNodes;
		
	}
	
	public void printTree() {
	    System.out.println("Stablo:");

	    printNode(root, 0);
	}

	private void printNode(Node node, int level) {
	    if (node.isLeaf() == true) {
	        Node leafNode = node;
	        for (Region entry : leafNode.getRegioni()) { // ovo su tacke tako da nema potrebe da vadim oba Point
	            Point point1 = entry.getTopLeft();
	            System.out.println(getIndentation(level) + "Tacka: (" + point1.getX() + ", " + point1.getY() + ")");
	        }
	    } else {
	    	for(int i = 0; i < node.getRegioni().size(); i++) {
	    		Point point1 = node.getRegioni().get(i).getTopLeft();
	            Point point2 = node.getRegioni().get(i).getBottomRight();
	            System.out.println(getIndentation(level) + "Region: (" + point1.getX() + ", " + point1.getY() + ")" + ", (" + point2.getX() + ", " + point2.getY() + ")");
	        	printNode(node.getRegioni().get(i).getPotomak(), level + 1);
	        }
	    }
	}

	private String getIndentation(int level) {
	    StringBuilder indentation = new StringBuilder();
	    for (int i = 0; i < level; i++) {
	        indentation.append("  "); 
	    }
	    return indentation.toString();
	}

	public void ucitajIzFajla(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] coordinates = line.split(",");
                if (coordinates.length == 2) {
                    double x = Double.parseDouble(coordinates[0].trim());
                    double y = Double.parseDouble(coordinates[1].trim());
                    Point point = new Point(x, y);
                    insert(point);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
