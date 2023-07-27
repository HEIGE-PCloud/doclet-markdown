package dev.pcloud.docletmarkdown.Markdown;

public class Code implements MarkdownObject {

    private MarkdownObject content;

    public Code(String content) {
        this.content = new Text(content);
    }

    public Code(MarkdownObject content) {
        this.content = content;
    }

    public MarkdownObject getContent() {
        return content;
    }

    public void setContent(MarkdownObject content) {
        this.content = content;
    }

    @Override
    public String render() {
        return "`" + content.render() + "`";
    }
}
