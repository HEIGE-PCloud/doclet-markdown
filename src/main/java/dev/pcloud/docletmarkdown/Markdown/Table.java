package dev.pcloud.docletmarkdown.Markdown;

public class Table implements MarkdownObject {
    private final MarkdownObject[][] grid;
    private int rows;
    private int columns;
    public Table(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        grid = new MarkdownObject[rows][columns];
    }

    public void setGrid(int x, int y, MarkdownObject content) {
        grid[x][y] = content;
    }
    public void setGrid(int x, int y, String text) {
        grid[x][y] = new Text(text);
    }

    private String renderRow(int row) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns; i++) {
            sb.append('|');
            if (grid[row][i] != null) {
                sb.append(grid[row][i].render());
            } else {
                sb.append("   ");
            }
        }
        sb.append('|');
        sb.append('\n');
        return sb.toString();
    }

    private String renderSeparator() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns; i++) {
            sb.append('|');
            sb.append("---");
        }
        sb.append('|');
        sb.append('\n');
        return sb.toString();
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append(renderRow(0));
        sb.append(renderSeparator());
        for (int row = 1; row < rows; row++) {
            sb.append(renderRow(row));
        }
        return sb.toString();
    }
}
