package assemblyline.common.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.prefab.network.PacketManager;
import assemblyline.common.AssemblyLine;
import dark.library.machine.TileEntityRunnableMachine;

/**
 * A class to be inherited by all machines on the assembly line. This will allow all machines to be
 * able to be powered through the powering of only one machine.
 * 
 * @author Calclavia
 * 
 */
public abstract class TileEntityAssemblyNetwork extends TileEntityRunnableMachine
{
	/**
	 * The range in which power can be transfered.
	 */
	public int powerTransferRange = 0;

	public boolean isRunning()
	{
		return AssemblyLine.REQUIRE_NO_POWER || this.powerTransferRange > 0 || this.wattsReceived > this.getRequest().getWatts();
	}

	public void updatePowerTransferRange()
	{
		int maximumTransferRange = 0;

		for (int i = 0; i < 6; i++)
		{
			ForgeDirection direction = ForgeDirection.getOrientation(i);
			TileEntity tileEntity = worldObj.getBlockTileEntity(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);

			if (tileEntity != null)
			{
				if (tileEntity instanceof TileEntityAssemblyNetwork)
				{
					TileEntityAssemblyNetwork assemblyNetwork = (TileEntityAssemblyNetwork) tileEntity;

					if (assemblyNetwork.powerTransferRange > maximumTransferRange)
					{
						maximumTransferRange = assemblyNetwork.powerTransferRange;
					}
				}
			}
		}

		this.powerTransferRange = Math.max(maximumTransferRange - 1, 0);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		this.onUpdate();

		if (this.ticks % 10 == 0)
		{
			if (this.wattsReceived >= this.getRequest().getWatts())
			{
				this.wattsReceived -= getRequest().getWatts();
				this.powerTransferRange = this.getMaxTransferRange();
			}
			else
			{
				this.powerTransferRange = 0;
				this.updatePowerTransferRange();
			}

			if (!this.worldObj.isRemote)
			{
				if (this.getDescriptionPacket() != null)
				{
					PacketManager.sendPacketToClients(this.getDescriptionPacket());
				}
			}
		}
	}

	protected void onUpdate()
	{

	}

	@Override
	public ElectricityPack getRequest()
	{
		return new ElectricityPack(1, this.getVoltage());
	}

	protected int getMaxTransferRange()
	{
		return 30;
	}
}
