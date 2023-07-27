package dev.pcloud.docletmarkdown.Markdown;

public class Text implements MarkdownObject{
    private String text;
    public Text(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String render() {
        return text;
    }
}
