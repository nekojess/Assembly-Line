package assemblyline.common.machine.encoder;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import assemblyline.common.AssemblyLine;

public class SlotDisk extends Slot
{

	public SlotDisk(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack itemStack)
	{
		return itemStack.itemID == AssemblyLine.itemDisk.itemID;
	}

}
