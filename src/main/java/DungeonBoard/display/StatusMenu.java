package DungeonBoard.display;

import DungeonBoard.main.Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusMenu {
    
    public static JPopupMenu createStatusMenu(final Integer tokenIndex) {
        final JPopupMenu menu = new JPopupMenu();

        final ArrayList<JMenuItem> menuItemList = new ArrayList<>();
        final List<String> menuItems = Arrays.asList("Blinded", "Charmed", "Deafened", "Frightened", "Grappled", "Incapacitated", "Invisible","Paralyzed", "Poisoned",
        "Prone", "Restrained", "Stunned", "Unconscious", "Copy");

        for(String menuItem : menuItems){
            final JMenuItem newItem = new JMenuItem(menuItem);
            if(menuItem == "Copy"){
                newItem.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent evt) {
                        Main.tokenList.copyToken(tokenIndex);
                    }
                });
            }
            else{
                newItem.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent evt) {
                        addStatus(tokenIndex, evt.getActionCommand());
                    }
                });
            }
            menuItemList.add(newItem);
        }
        for(JMenuItem menuItem : menuItemList){
            menu.add(menuItem);
        }
    
        return menu;

    }

    static private void addStatus(final Integer tokenIndex, final String status) {
        final var statusList = Main.tokenList.getToken(tokenIndex).getStatusList();
        if(!statusList.contains(status)) {
            statusList.add(status);
            Main.tokenList.setToken(tokenIndex, Main.tokenList.getToken(tokenIndex).setStatusList(statusList));
        }
        else{
            statusList.removeIf(i -> i == status);
        }
        Main.repaintDisplays();
    }
}