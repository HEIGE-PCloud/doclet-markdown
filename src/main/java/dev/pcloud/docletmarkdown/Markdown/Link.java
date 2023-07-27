package dev.pcloud.docletmarkdown.Markdown;

public class Link implements MarkdownObject {
    private final MarkdownObject content;
    private final String link;

    public Link(MarkdownObject content, String link) {
        this.content = content;
        this.link = link;
    }
    public Link(String text, String link) {
        this.content = new Text(text);
        this.link = link;
    }
    @Override
    public String render() {
        return "[" + content.render() + "]" + "(" + link + ")";
    }
}
