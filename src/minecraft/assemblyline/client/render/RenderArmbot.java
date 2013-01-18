package assemblyline.client.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector3;
import assemblyline.client.model.ModelArmbot;
import assemblyline.common.AssemblyLine;
import assemblyline.common.machine.armbot.TileEntityArmbot;

public class RenderArmbot extends TileEntitySpecialRenderer
{
	public static final ModelArmbot MODEL = new ModelArmbot();
	public static final String TEXTURE = "armbot.png";

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float var8)
	{
		if (tileEntity instanceof TileEntityArmbot)
		{
			this.bindTextureByName(AssemblyLine.TEXTURE_PATH + TEXTURE);
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
			GL11.glScalef(1.0F, -1F, -1F);
			MODEL.render(0.0625f, ((TileEntityArmbot) tileEntity).renderYaw, ((TileEntityArmbot) tileEntity).renderPitch);
			Vector3 handPos = ((TileEntityArmbot) tileEntity).getHandPosition();
			handPos.subtract(new Vector3(tileEntity));
			GL11.glPushMatrix();
			GL11.glRotatef(180, 0, 0, 1);
			for (Entity entity : ((TileEntityArmbot) tileEntity).grabbedEntities)
			{
				if (entity != null && entity instanceof EntityItem) //items don't move right, so we render them manually
				{
					Render render = RenderManager.instance.getEntityRenderObject(entity);
					if (render != null)
					{
						render.doRender(entity, -handPos.x + 0.5f, handPos.y - 1.5f, -handPos.z + 0.5f, 0, 0);
					}
				}
			}
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}

}