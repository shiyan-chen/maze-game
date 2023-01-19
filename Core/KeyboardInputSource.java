package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInputSource implements InputSource {

    public char getCurrentKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char current = Character.toLowerCase(StdDraw.nextKeyTyped());
                return current;
            }
        }
    }

    public boolean hasNext() {
        return true;
    }
}
