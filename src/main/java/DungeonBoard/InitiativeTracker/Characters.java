package DungeonBoard.InitiativeTracker;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JButton;
import org.javatuples.Quartet;
import java.util.Collections;
import java.util.Comparator;

class Characters{
    Characters(){
        characters = new  ArrayList<Character>();
    }

    public void addCharacter(String name, Integer initiative, JButton button, String user){
        characters.add(new Character(name, initiative, button, user)); }

    public void sortCharacters(){
        Collections.sort(characters, new Comparator<Character>() {
            public int compare(final Character obj1,
                    final Character obj2) {
                return -1 * obj1.getInitiative().compareTo(obj2.getInitiative());
            }
        });
    }

    public void setCharacter(int index, Character character){
        characters.set(index, character);
    }

    public List<Character> getCharacters(){
        return characters;
    }

    public Character getCharacter(int index){
        return characters.get(index);
    }

    private List<Character> characters;

    class Character{
         Quartet<String, Integer, JButton, String> character;
        Character(String name, Integer initiative, JButton button, String user){
            character = Quartet.with(name, initiative, button, user);
        }

        private Character(Quartet<String, Integer, JButton, String> character2){
            character = character2;
        }

         String getName(){ return character.getValue0(); }
         Integer getInitiative(){ return character.getValue1(); }
         JButton getButton(){ return character.getValue2(); }
         String getUser(){ return character.getValue3(); }

         Character setInitiative(int initiative){
            return new Character(character.setAt1(initiative));
         }
    }
}