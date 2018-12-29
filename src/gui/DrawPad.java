package gui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class DrawPad extends JPanel implements MouseMotionListener, MouseListener {
	public String mode = "none";
	public int currentLineIndex = -1;
	public int currentRouterIndex = -1;
	private static final int MAX = 100;
	private Router[] router = new Router[MAX];
	private static final long serialVersionUID = 1L;
	private Connection line[] = new Connection[MAX];
	private int indexer = 0;
	private int numOfRouters = 0;
	private Point startPoint = new Point(0, 0);
	private Point endPoint = new Point(0, 0);
    private int numOfLines = 0;
    public Connection currentLine = null;
	
	public DrawPad() {
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setBackground(Color.WHITE);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D) g).setStroke(new BasicStroke(5));
		if(currentLine != null) {
			startPoint.set(currentLine.router1.position.x + 25, currentLine.router1.position.y + 25);
			endPoint.set(getMousePosition().x, getMousePosition().y);
			g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
		}
	}

    public int getRouterIndexInList(int index) {
    	for (int i = 0; i < numOfRouters; i++) {
    		if(router[i].index == index)
    			return i;
    	}
    	return -1;
    } 
    
    public void addConnection(Connection l) {
		line[numOfLines] = l;
		numOfLines++;
    }
     
    public void removeConnection(int indexInList) {
    	remove(line[indexInList]);
    	if(indexInList > -1) {
	    	for (int k = indexInList ; k < numOfLines ;k++) {
	    		line[k] = line[k+1];
	    	}
	    	line[numOfLines - 1] = null;
	    	numOfLines--;
    	}
    }

    public void removeRouter(int indexInList) {
    	for (int i = indexInList ; i < numOfRouters ;i++) {
    		router[i] = router[i+1];
    	}
    	router[numOfRouters - 1] = null;
    	numOfRouters--;
    }
    
    public void Remove(int index) {
    	int indexInList = getRouterIndexInList(index);
    	Router r = router[indexInList];
    	if (indexInList < 0 || indexInList >= numOfRouters) {
    		return;
    	}
    	Connection[] delet_connection = r.getConnectionsArray();
    	for(int i = 0 ; i < r.getConnectionsCount() ; i ++) {
    		for(int k = 0 ; k <numOfLines ;k++) {
    			if(delet_connection[i] == line[k]) {
    				removeConnection(k);
    			}
    		}
    	}
    	//delete the router
    	removeRouter(indexInList);
    	repaint();
    }

    public void addRouter(Point position) {
    	if (numOfRouters < MAX) {
    		router[numOfRouters] = new Router(position, indexer, this);
    		indexer++;
    		numOfRouters++;
    	}
    }
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Point position = new Point(x, y);
		if (e.getButton() == 1 && currentRouterIndex == -1 && currentLineIndex == -1) {
			if (e.getClickCount() == 1) {
				//adding new router
				if (currentRouterIndex < 0) { 
					//no router exists in this area
					Point midPoint = new Point(-25, -25);
					position.add(midPoint);
					addRouter(position);
				}
			}
		}
		repaint();
	}
	@Override
	public void mouseDragged(MouseEvent event) {}
	@Override
	public void mouseMoved(MouseEvent e) {
		if(mode == "connect") {
    		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    		repaint();
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	public Router[] getRouterArray() {
		return router;
	}
	
	public int getRoutersCount() {
		return numOfRouters;
	}

}
