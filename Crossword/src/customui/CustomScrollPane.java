package customui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CustomScrollPane extends JScrollPane {
	CustomScrollPaneLayout paneLayout;
	MyScrollBarUI scrollBarUI;
	
	public void setScrollBarUI(MyScrollBarUI scrollBarUI) {
		this.scrollBarUI = scrollBarUI;
	}
	
	public MyScrollBarUI getScrollBarUI() {
		return scrollBarUI;
	}
	
	public CustomScrollPane(Component view, int width, int height) {
		super(view);
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(220, 220, 220)));
		
		
		this.setComponentZOrder(this.getVerticalScrollBar(), 0);
	    this.setComponentZOrder(this.getViewport(), 1);
	    this.getVerticalScrollBar().setOpaque(false);
	    
	    paneLayout = new CustomScrollPaneLayout(this);
	    scrollBarUI = new MyScrollBarUI();

	    this.setLayout(paneLayout);
	    this.getVerticalScrollBar().setUI(scrollBarUI);
	}
	
	public class CustomScrollPaneLayout extends ScrollPaneLayout {
		CustomScrollPane scrollPane = null;
		
		public CustomScrollPaneLayout(CustomScrollPane scrollPane) {
			this.scrollPane = scrollPane;
		}
		
		@Override
	      public void layoutContainer(Container parent) {
	        JScrollPane scrollPane = (JScrollPane) parent;

	        Rectangle availR = scrollPane.getBounds();
	        availR.x = availR.y = 0;

	        Insets parentInsets = parent.getInsets();
	        availR.x = parentInsets.left;
	        availR.y = parentInsets.top;
	        availR.width -= parentInsets.left + parentInsets.right;
	        availR.height -= parentInsets.top + parentInsets.bottom;

	        Rectangle vsbR = new Rectangle();
	        vsbR.width = 12;
	        vsbR.height = availR.height;
	        vsbR.x = availR.x + availR.width - vsbR.width;
	        vsbR.y = availR.y;

	        if (viewport != null) {
	          viewport.setBounds(availR);
	        }
	        if (vsb != null) {
	          vsb.setVisible(true);
	          vsb.setBounds(vsbR);
	        }
	      }
	}
	
	class MyScrollBarUI extends BasicScrollBarUI {
		 private final Dimension d = new Dimension();

	      @Override
	      protected JButton createDecreaseButton(int orientation) {
	        return new JButton() {
	          
	            private static final long serialVersionUID = -3592643796245558676L;

	            @Override
	              public Dimension getPreferredSize() {
	                return d;
	              }
	            };
	      }

	      @Override
	      protected JButton createIncreaseButton(int orientation) {
	        return new JButton() {
	          
	            private static final long serialVersionUID = 1L;

	        @Override
	          public Dimension getPreferredSize() {
	            return d;
	          }
	        };
	      }

	      @Override
	      protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
	      }

	      @Override
	      protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
	        Graphics2D g2 = (Graphics2D) g.create();
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        Color color = null;
	        JScrollBar sb = (JScrollBar) c;
	        if (!sb.isEnabled() || r.width > r.height) {
	          return;
	        } else if (isDragging) {
	          color = Color.DARK_GRAY; // change color
	        } else if (isThumbRollover()) {
	          color = Color.LIGHT_GRAY; // change color
	        } else {
	          color = Color.GRAY; // change color
	        }
	        g2.setPaint(color);
	        g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
	        g2.setPaint(Color.WHITE);
	        g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
	        g2.dispose();
	      }

	      @Override
	      protected void setThumbBounds(int x, int y, int width, int height) {
	        super.setThumbBounds(x, y, width, height);
	        scrollbar.repaint();
	      }
	      
	      public void repaintScrollBar() {
	    	  scrollbar.repaint();
	      }
	}
}
