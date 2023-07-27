package dev.pcloud.docletmarkdown.Markdown;

public class Newline implements MarkdownObject {
    @Override
    public String render() {
        return "\n";
    }
}
