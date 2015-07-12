package latmod.ftbu.core;

import latmod.ftbu.core.net.*;
import latmod.ftbu.core.util.FastMap;
import latmod.ftbu.mod.FTBU;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import cpw.mods.fml.relauncher.*;

public abstract class LMGuiHandler
{
	public final String ID;
	
	public LMGuiHandler(String s)
	{ ID = s; }
	
	@SideOnly(Side.CLIENT)
	public void registerClient()
	{ Registry.guiHandlers.put(ID, this); }
	
	public abstract Container getContainer(EntityPlayer ep, int id, NBTTagCompound data);
	
	@SideOnly(Side.CLIENT)
	public abstract GuiScreen getGui(EntityPlayer ep, int id, NBTTagCompound data);
	
	public void openGui(EntityPlayer ep, int id, NBTTagCompound data)
	{
		if(ep == null || ep instanceof FakePlayer) return;
		
		if(ep instanceof EntityPlayerMP)
		{
			Container c = getContainer(ep, id, data);
			if(c == null) return;
			
			EntityPlayerMP epM = (EntityPlayerMP)ep;
			epM.getNextWindowId();
			epM.closeContainer();
			epM.openContainer = c;
			epM.openContainer.windowId = epM.currentWindowId;
			epM.openContainer.addCraftingToCrafters(epM);
			MessageLM.NET.sendTo(new MessageOpenGui(ID, id, data, epM.currentWindowId), epM);
		}
		else if(!LatCoreMC.isServer())
			FTBU.proxy.openClientGui(ep, ID, id, data);
	}
	
	@SideOnly(Side.CLIENT)
	public static class Registry
	{
		private static final FastMap<String, LMGuiHandler> guiHandlers = new FastMap<String, LMGuiHandler>();
		
		public static LMGuiHandler getLMGuiHandler(String id)
		{ return guiHandlers.get(id); }
	}
}