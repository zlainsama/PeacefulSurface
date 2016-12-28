package lain.mods.peacefulsurface;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandReloadPeace extends CommandBase
{

    ITextComponent msgDone = new TextComponentTranslation("reloadPeace.done", new Object[0]).setStyle(new Style().setColor(TextFormatting.YELLOW));

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        PeacefulSurface.instance.reloadConfig();
        sender.sendMessage(msgDone);
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

    @Override
    public String getUsage(ICommandSender arg0)
    {
        return "reloadPeace.usage";
    }

}
