package rxeditor.Models;

/**
 * Class modeling an SQL value, having as its properties the content
 * as a string and its SQL data type as a Datatype determining the form
 * of toString() output.  
 */

public class SQLValue {

    private final String content;
    private final Datatype datatype;

    public SQLValue(String content, Datatype datatype) {
        this.content = content;
        this.datatype = datatype;
    }

    public String getContent() {
        return content;
    }

    public Datatype getDatatype() {
        return datatype;
    }

    @Override
    public String toString() {
        
        if (content == null || content.isEmpty()) {
            return "NULL";
        }

        switch (datatype) {
            case VARCHAR:
                return "'" + content + "'";
            case INT:
                return content;
            case FLOAT:
                return content;
            case DATE:
                return "'" + content + "'";
            case BOOLEAN:
                return "'" + content + "'";
        }

        return content;
    }

}
