package DungeonBoard.InitiativeTracker;

import DungeonBoard.main.Settings;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import net.dv8tion.jda.api.entities.User;

public class InitiativePanelButtonManager {

    public static JButton createCharacterButton(String name, Integer init, String userName) {
        final JButton newCharacter = Settings.createButton(name + ": " + init.toString());
        newCharacter.setForeground(Settings.TEXT);
        newCharacter.setBackground(Settings.TEXT_BACKGROUND);
        newCharacter.setMaximumSize(new Dimension(5000, 30));
        newCharacter.setHorizontalAlignment(SwingConstants.CENTER);
        addListener(newCharacter, userName);
        return newCharacter;
    }

    private static void addListener(final JButton newCharacter, String userName) {
        newCharacter.addMouseListener(new MouseAdapter() {
            public void mousePressed(final MouseEvent e) {
                if (e.getButton() == 3) {
                    sendDiscordMessage(userName);
                }
                else{
                    final String name = ((JButton) e.getSource()).getText();
                    setInitiative(name, e);
                    ((JButton) e.getSource()).getParent().repaint();
                }
            }
        });
    }

    private static void sendDiscordMessage(String userName){
        String message = JOptionPane.showInputDialog("Message:", "");
                    
        var guilds = InitiativePanel.jda.getGuilds();
        var guild = guilds.get(0);
        var users = guild.getMembers();
        User user = null;
        for(var member : users){
            if(member.getUser().getName().equalsIgnoreCase(userName)) 
                user = member.getUser();
        } 
        user.openPrivateChannel().queue((channel) 
            ->{channel.sendMessage(message).queue();});        
        System.out.println(guild.getName());  
    }

    private static void setInitiative(String name, MouseEvent e){
        final Integer initiative = Integer.parseInt(JOptionPane.showInputDialog("Set Initiative", ""));
        int i = 0;
        for (var character : InitiativePanel.getCharacters()) {
            if (name.contains(character.getName())) {
                InitiativePanel.getCharacters().set(i, character.setInitiative(initiative));
                ((JButton) e.getSource()).setText(character.getName() + " - " + InitiativePanel.getCharacters().get(i).getInitiative().toString());
            }
            i++;
        }
        InitiativePanel.sortCharacters();
        ((InitiativePanel) ((JButton) e.getSource()).getParent()).updatePanel();
    }
}