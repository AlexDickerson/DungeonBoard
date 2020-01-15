package DungeonBoard.InitiativeTracker;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.security.auth.login.LoginException;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import javax.swing.Box;

//import java.io.BufferedWriter;
//import java.io.FileWriter;

public class InitiativePanel extends JPanel {
	
	public InitiativePanel() {
        setOpaque(true);
        setVisible(true);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));  
        jda = createBot();      
        characters = new Characters();
        addCharactersFromJSON(InitiativePanel.class.getResourceAsStream("/characters.json"));
        addInitialCharacters();
    }

    public void addInitialCharacters(){
        add(Box.createVerticalStrut(5));

        int i = 0;
        for(var character : characters.getCharacters()) {
            characters.setCharacter(i, characters.new Character(character.getName(), 
            character.getInitiative(),
            InitiativePanelButtonManager.createCharacterButton(character.getName(), character.getInitiative(), character.getUser()),
            character.getUser()));
            
            add(characters.getCharacters().get(i).getButton());
            i++;
            add(Box.createVerticalStrut(5));
        }
        repaint();
    }
    
    private JDA createBot(){
        JDABuilder discordBot = new JDABuilder(AccountType.BOT);
        String botToken = "NjY0OTg3MTgxNzk3OTk4NjEy.XhfUig.Q3BpLwveqahaKdpl-Bv2LBF9Eew";
        discordBot.setToken(botToken);
        jda = null;
        try{ 
            jda = discordBot.build();
            jda.awaitReady();
        } catch(LoginException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return jda;
    }

    public void updatePanel() {
        removeAll();
        revalidate();
        repaint();

        add(Box.createVerticalStrut(5));
        for (var character : characters.getCharacters()) {
            add(character.getButton());
            add(Box.createVerticalStrut(5));
        }

        revalidate();
        repaint();
    }

    private void addCharactersFromJSON(final InputStream fileName) {
        Object obj = new Object();
        JSONArray jo = new JSONArray();
        try {
            obj = new JSONParser().parse(new InputStreamReader(fileName));
            jo = (JSONArray) obj;
        } catch (final Exception e) { }

        for (final Object character : jo) {
            final JSONObject jsonCharacter = (JSONObject) character;
            characters.addCharacter((String) jsonCharacter.get("name"), 0, null, (String) jsonCharacter.get("user"));
        }
    }

    public static List<Characters.Character> getCharacters(){
        return characters.getCharacters();
    }

    public void addCharacter(String name){
        int suffix = 0;
        for(Characters.Character character : characters.getCharacters()){
            if(character.getName().contains(name)) suffix++;
        }
        characters.addCharacter(name + "-" + Integer.toString(suffix), 0, InitiativePanelButtonManager.createCharacterButton(name + "-" + Integer.toString(suffix), 0, null), "");
        updatePanel();
    }

    public static void sortCharacters(){
        characters.sortCharacters();
    }

    private static final long serialVersionUID = 1L;
    static JDA jda;
    private static Characters characters;
    /*private void addCharactersToJson(final String filename) throws Exception {
        final BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

        final JSONArray ja = new JSONArray();
        for (var character : getCharacters()) {
            final JSONObject sampleObject = new JSONObject();
            sampleObject.put("name", character.getValue0());
            ja.add(sampleObject);
        }
        writer.write(ja.toJSONString());
        writer.close();
    }*/
    
    
}