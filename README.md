# rxeditor

Simple text editor with the option to store and apply regular expressions and generate CSV data and SQL insert statements using regex.

### Getting Started

Clone this repository and run `./gradlew run` or download the jar release.

### Using the editor

Enter text you wish to apply regular expressions to in the main text field, and enter a regular expression in the text field at the bottom of the main editor window.

Pressing ENTER or clicking APPLY will apply the given expression to the main text field.

EXTRACT will open a new text area in a new tab containing all matches of the previously applied expression separated by newlines.

REPLACE will open a dialog with a match and replace functionality.

CSV will open a dialog where you may enter a delimiter character sequence and a regular expression to be used for placing the delimiter. Selecting "Beginning" will place the delimiter at the beginning of the match, "End" at the end and "Both" in both points. Clicking "Generate" will open a new tab containing the generated data.

SQL will open a dialog where you may generate SQL insert statements based on CSV data. Enter a table name, a number of columns and a delimiter character (the delimiter used in the CSV you are processing). Ticking "Serial" will insert "default" in the first column. You must first set a data type for each column value by clicking "Datatypes" and making the selections. Now, clicking "Generate" will open a new tab containing the generated SQL insert statements.

Under the menu item REGEX, you may select "Instant Regex Feedback", which will instantly apply your expression to the main text field after any key is pressed while the regex text field is selected. Note that depending on the expression, applying it may temporarily freeze the application if the main text area contains enough text.

Under REGEX, you may also save and load regular expressions according to name and category.


### Acknowledgements

* The application uses InlineCssTextArea and VirtualizedScrollPane from the RichTextFX library under the licenses BSD 2-Clause and GPLv2. https://github.com/FXMisc/RichTextFX
