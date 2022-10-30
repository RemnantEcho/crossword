package customui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.Main;

public class CustomJList extends JList {
	Main main = null;
	CustomJList otherListRef = null;
	CustomScrollPane scrollPane = null;
	boolean isSwitching = false;
	DefaultListModel<String> model = null;

	public void setOtherListRef(CustomJList oList) {
		otherListRef = oList;

	}

	public void setScrollPane(CustomScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
	
	public void setSwitching(boolean isSwitching) {
		this.isSwitching = isSwitching;
	}

	public CustomJList getOtherListRef() {
		return otherListRef;
	}

	public CustomScrollPane getScrollPane() {
		return scrollPane;
	}
	
	public boolean isSwitching() {
		return isSwitching;
	}
	
	@Override
	protected void processMouseMotionEvent(MouseEvent e) {
	    
	}

	public CustomJList(Main main, int width, int height) {
		this.main = main;
		UIManager.put("List.focusCellHighlightBorder", BorderFactory.createEmptyBorder());
		this.setLayoutOrientation(JList.VERTICAL);
		
		model = new DefaultListModel<String>();
		
		this.setModel(model);
		this.setCellRenderer(new MyCellRenderer(width));
		this.setFont(new Font("Calibri", Font.PLAIN, 14));
		this.addFocusListener(new CustomFocusListener(this));
		this.setSelectionModel(new CustomDefaultListSelectionModel(this));
		this.setVisibleRowCount(0);
	}
	
	public void clear() {
		this.clearSelection();
	}

	// Render List Elements
	class MyCellRenderer extends DefaultListCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public static final String HTML_1 = "<html><body style='width: ";
		public static final String HTML_2 = "px'>";
		public static final String HTML_3 = "</html>";
		private int width;

		public MyCellRenderer(int width) {
			this.width = width;
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			String text = HTML_1 + String.valueOf(width) + HTML_2 + value.toString() + HTML_3;
			return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
		}

	}

	class CustomDefaultListSelectionModel extends DefaultListSelectionModel {
		private static final long serialVersionUID = 1L;
		CustomJList jList = null;

		public CustomDefaultListSelectionModel(CustomJList jList) {
			this.jList = jList;
		}

		public void setSelectionInterval(int index0, int index1) {
			if (index0 == index1) {
				if (isSelectedIndex(index0)) {
					removeSelectionInterval(index0, index0);
//					jList.getScrollPane().repaint();
					return;
				}
			}
			
			if (otherListRef != null) {
				if (!otherListRef.isSelectionEmpty()) {
					if (!jList.isSwitching()) {
						otherListRef.clear();
						otherListRef.getScrollPane().revalidate();
						otherListRef.getScrollPane().repaint();
					}
				}
			}
			jList.getScrollPane().revalidate();
			jList.getScrollPane().repaint();
			super.setSelectionInterval(index0, index1);
			main.highlightWord((String) jList.getSelectedValue());
			try {
				if (main.getJTable().getCellEditor() != null) {
					main.getJTable().getCellEditor().cancelCellEditing();
				}
			} catch (Exception exe) {
				System.out.println(exe);
			};
		}

		@Override
		public void addSelectionInterval(int index0, int index1) {
			/*
			if (index0 == index1) {
				if (isSelectedIndex(index0)) {
					removeSelectionInterval(index0, index0);
					return;
				}
			}
			*/
//			jList.getScrollPane().repaint();
			super.addSelectionInterval(index0, index1);
		}
		
		@Override
		public void removeSelectionInterval(int index0, int index1)
	    {
			if (main != null) {
				main.clearHighlight();
			}
			
	        super.removeSelectionInterval(index0, index1);
	    }
		
	}
	
	class CustomFocusListener implements FocusListener {
		CustomJList jList;
		
		public CustomFocusListener(CustomJList jList) {
			this.jList = jList;
		}

		@Override
		public void focusGained(FocusEvent e) {
		}

		@Override
		public void focusLost(FocusEvent e) {
			jList.getScrollPane().repaint();
		}
		
	}
}
