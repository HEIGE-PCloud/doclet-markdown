package dev.pcloud.docletmarkdown.Markdown;

public class Heading implements MarkdownObject {
    private int level;
    private MarkdownObject content;
    public Heading(int level) {
        this(level, new Empty());
    }

    public Heading(int level, MarkdownObject content) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("The heading level must between 1 and 6");
        }
        this.level = level;
        this.content = content;
    }
    public Heading(int level, String text) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("The heading level must between 1 and 6");
        }
        this.level = level;
        this.content = new Text(text);
    }
    public void setContent(MarkdownObject content) {
        this.content = content;
    }

    public void setLevel(int level) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("The heading level must between 1 and 6");
        }
        this.level = level;
    }

    @Override
    public String render() {
        return "#".repeat(Math.max(0, level)) +
                ' ' +
                content.render() + '\n';
    }
}
