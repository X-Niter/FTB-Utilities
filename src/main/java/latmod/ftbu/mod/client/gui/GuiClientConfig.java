package latmod.ftbu.mod.client.gui;

import latmod.ftbu.core.FTBULang;
import latmod.ftbu.core.client.ClientConfig;
import net.minecraft.client.gui.*;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class GuiClientConfig extends GuiScreen
{
	public final GuiScreen parent;
	
	public GuiClientConfig(GuiScreen g)
	{
		parent = g;
	}
	
	@SuppressWarnings("unchecked")
	public void initGui()
	{
		buttonList.clear();
		int j = -1;
		for(int i = 0; i < ClientConfig.Registry.map.size(); i++)
		{
			ClientConfig cc = ClientConfig.Registry.map.values.get(i);
			if(!cc.isHidden) buttonList.add(new GroupButton(++j, cc));
		}
		
		buttonList.add(new GuiButton(255, width / 2 - 100, height - 40, 200, 20, FTBULang.button_back));
	}
	
	public void actionPerformed(GuiButton b)
	{
		if(b.id == 255) mc.displayGuiScreen(parent);
		else if(b instanceof GroupButton)
			mc.displayGuiScreen(new GuiClientConfigTab(this, ((GroupButton)b).config));
	}
	
	public void onGuiClosed()
	{
	}
	
	public void drawScreen(int mx, int my, float pt)
	{
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Client Config", width / 2, 15, 16777215);
		super.drawScreen(mx, my, pt);
	}
	
	private class GroupButton extends GuiButton
	{
		public final ClientConfig config;
		
		public GroupButton(int id, ClientConfig c)
		{
			super(id, GuiClientConfig.this.width / 2 - 100, id * 28 + 32, 200, 20, c.getIDS());
			config = c;
		}
	}
	
	public static class GuiClientConfigTab extends GuiScreen
	{
		public final GuiScreen parent;
		public final ClientConfig config;
		
		public GuiClientConfigTab(GuiClientConfig g, ClientConfig c)
		{
			parent = g.parent;
			config = c;
		}
		
		@SuppressWarnings("unchecked")
		public void initGui()
		{
			buttonList.clear();
			
			for(int i = 0; i < config.map.size(); i++)
				buttonList.add(new PropButton(i, config.map.values.get(i)));
			
			buttonList.add(new GuiButton(255, width / 2 - 100, height - 40, 200, 20, FTBULang.button_back));
		}
		
		public void actionPerformed(GuiButton b)
		{
			if(b.id == 255) mc.displayGuiScreen(new GuiClientConfig(parent));
			else if(b instanceof PropButton)
			{
				ClientConfig.Property p = ((PropButton)b).property;
				p.incValue();
				b.displayString = p.toString();
			}
		}
		
		public void drawScreen(int mx, int my, float pt)
		{
			drawDefaultBackground();
			drawCenteredString(fontRendererObj, config.getIDS(), width / 2, 15, 16777215);
			super.drawScreen(mx, my, pt);
		}
		
		public void onGuiClosed()
		{
			ClientConfig.Registry.save();
		}
		
		private class PropButton extends GuiButton
		{
			public final ClientConfig.Property property;
			
			public PropButton(int id, ClientConfig.Property p)
			{
				super(id, GuiClientConfigTab.this.width / 2 - 100, id * 28 + 32, 200, 20, p.toString());
				property = p;
			}
		}
	}
}