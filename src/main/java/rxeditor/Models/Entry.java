package rxeditor.Models;

/**
 * Class for processing multiple string values when loading or saving
 * expressions.
 */
public class Entry {

    private String category;
    private String name;
    private String regex;
    private String delimiter;

    public Entry(String category, String name, String regex) {
        this.category = category.toUpperCase();
        if (name.isEmpty()) {
            this.name = regex;
        } else {
            this.name = name;
        }
        this.regex = regex;
        delimiter = " ";
    }

    public String getRegex() {
        return regex;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    //Looks at name and category to verify name does not already exist in category for saving 
    // and loading purposes.
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Entry) {
            Entry objEntry = (Entry) obj;
            if (this.category.isEmpty() && !objEntry.getCategory().isEmpty()
                    && this.name.equals(objEntry.getCategory())) {
                return true;
            }
            if (objEntry.getName().equals(this.name) && objEntry.getCategory().equals(this.category)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(category);
        sb.append(delimiter);
        sb.append(name);
        sb.append(delimiter);
        sb.append(regex);
        sb.append("\n");
        return sb.toString();
    }

}
