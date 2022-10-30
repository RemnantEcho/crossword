package customui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class CustomButton extends JButton {
	public CustomButton(String title, int x, int y, int w, int h) {
		init(title);
		this.setLocation(x, y);
		this.setPreferredSize(new Dimension(w, h));
	}
	
	public CustomButton(String title, int w, int h) {
		init(title);
		this.setSize(new Dimension(w, h));
		this.setPreferredSize(new Dimension(w, h));
		this.setMinimumSize(new Dimension(w, h));
		this.setMaximumSize(new Dimension(w, h));
	}
	
	public CustomButton(String title) {
		init(title);
	}
	
	public void init(String t) {
		this.setText(t);
		this.setFont(new Font("Calibri", Font.BOLD, 26));
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setForeground(new Color(255, 255, 255));
		this.setBackground(new Color(0, 174, 239));
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
        this.setOpaque(true);
		this.setFocusPainted(false);
		this.setPreferredSize(new Dimension(600, 65));
		this.setBorder(new LineBorder(new Color(0, 174, 239), 5));
		this.addMouseListener(new CustomMouseListener(this));
	}
	
	public class CustomMouseListener extends MouseAdapter {
		JButton tempBtn;
		public CustomMouseListener(JButton btn) {
			tempBtn = btn;
		}
		
		public void mouseEntered(MouseEvent e) {
			tempBtn.setBackground(new Color(0, 159, 239));
		}
		
		public void mouseExited(MouseEvent e) {
			tempBtn.setBackground(new Color(0, 174, 239));
		}
		
		public void mouseMoved(MouseEvent e) {
			tempBtn.setBackground(new Color(0, 159, 239));
		}
		
		public void mousePressed(MouseEvent e) {
			tempBtn.setBackground(new Color(0, 131, 191));
		}
		
		public void mouseReleased(MouseEvent e) {
			tempBtn.setBackground(new Color(0, 174, 239));
		}
	}
}
