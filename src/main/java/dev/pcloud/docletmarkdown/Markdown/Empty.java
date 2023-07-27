package dev.pcloud.docletmarkdown.Markdown;

public class Empty implements MarkdownObject {
    @Override
    public String render() {
        return "";
    }
}
