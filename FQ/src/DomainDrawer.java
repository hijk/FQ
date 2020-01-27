 import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;

import javax.swing.JComponent;

class DomainDrawer extends JComponent {
	private ITree tree;

	public DomainDrawer(ITree tree) {
		this.tree = tree;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);		
        
        double x0 = tree.weightRanges[0][0];
        double x1 = tree.weightRanges[0][1];
        double y0 = tree.weightRanges[0][0];
        double y1 = tree.weightRanges[0][1];
        double wxRange =  x1 - x0;
        double wyRange = y1 - y0;
        
        double scale = 1.5;
        double factor1 = (double)this.getWidth()/wxRange;
        double factor2 = (double)this.getHeight()/wyRange;
        double factor = Math.min(factor1, factor2)/scale;
        
        double[] offset = {-x0 - wxRange/2, -y0 - wyRange/2};
        
        g2.translate(this.getWidth()/2, this.getHeight()/2);
        g2.scale(factor, -factor);
        g2.translate(offset[0], offset[1]); 
        
//        AffineTransform transform = g2.getTransform().createInverse();
        g2.setColor(Color.RED);
        try {
            g2.setStroke(new TransformedStroke(new BasicStroke(2f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0),
                                              g2.getTransform()));
        }
        catch(NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
        
        double boundary = 1000;
        g2.draw(new Line2D.Double(x0, y0, x0, y1));
        g2.draw(new Line2D.Double(x1, y0, x1, y1));
        g2.draw(new Line2D.Double(x0, y1, x1, y1));
        g2.draw(new Line2D.Double(x0, y0, x1, y0));
        g2.draw(new Line2D.Double(-boundary,0,boundary,0));
        g2.draw(new Line2D.Double(0,-boundary,0,boundary));
        
        g2.setColor(Color.BLACK);
        try {
            g2.setStroke(new TransformedStroke(new BasicStroke(2f),
                                              g2.getTransform()));
        }
        catch(NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
        
		for (int i = 0; i < tree.data.length; i++) {
			for (int j = i + 1; j < tree.data.length; j++) {
				double[] d = tree.intersection(i, j);
				double c = tree.constants[i] - tree.constants[j];
				if (d[1] == 0) {
					double x = -c / d[0];
					g2.draw(new Line2D.Double( x, -boundary, x, boundary ));
				} else {
					g2.draw(new Line2D.Double(-boundary, (d[0] * boundary - c) / d[1], boundary, (-d[0] * boundary - c) / d[1]) );
				}
			}
		}
		
		g2.dispose();
	}
}