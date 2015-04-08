package lain.mods.peacefulsurface;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandReloadPeace extends CommandBase
{

    IChatComponent msgDone = new ChatComponentTranslation("reloadPeace.done", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW));

    @Override
    public String getCommandName()
    {
        return "reloadPeace";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "reloadPeace.usage";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2)
    {
        PeacefulSurface.instance.reloadConfig();
        var1.addChatMessage(msgDone);
    }

}
