package com.dios.y2onlineshop.components;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.TreeField;
import net.rim.device.api.ui.component.TreeFieldCallback;

public class TreeCallback implements TreeFieldCallback {
	
	public void drawTreeItem(TreeField _tree, Graphics g, int node, int y, int width, int indent) 
    {
        String text = (String)_tree.getCookie(node); 
        g.drawText(text, indent, y);
    }

}
