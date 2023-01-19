package byow.Core;

/**
 * @Source Hug's InputDemo/StringInputDevice
 */

public class StringInputSource implements  InputSource {
    private String inputString;
    private int stringIndex;

    public StringInputSource(String s) {
        this.inputString = s;
        this.stringIndex = 0;
    }

    @Override
    public char getCurrentKey() {
        int currentIndex = stringIndex;
        stringIndex += 1;
        return inputString.charAt(currentIndex);
    }
    @Override
    public boolean hasNext() {
        return stringIndex < inputString.length();
    }

}
