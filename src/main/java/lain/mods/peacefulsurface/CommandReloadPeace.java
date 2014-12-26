package lain.mods.peacefulsurface;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandReloadPeace extends CommandBase
{

    IChatComponent msgDone = new ChatComponentTranslation("reloadPeace.done", new Object[0]).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW));

    @Override
    public void execute(ICommandSender var1, String[] var2) throws CommandException
    {
        PeacefulSurface.instance.reloadConfig();
        var1.addChatMessage(msgDone);
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "reloadPeace.usage";
    }

    @Override
    public String getName()
    {
        return "reloadPeace";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

}
