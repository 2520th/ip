package ip;

public class Parser {

    /**
     * Create a Command object from raw user inputs
     * @param input is the original string
     * @return Command object with first token as command, later ones as content
     */
    public Command parse(String input) {
        String[] tokens = input.trim().split(" ");
        if (tokens.length == 0) {
            return null;
        }
        String[] content = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, content, 0, content.length);
        return new Command(tokens[0].toLowerCase(), content);
    }
}
