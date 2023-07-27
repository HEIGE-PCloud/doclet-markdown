package dev.pcloud.docletmarkdown.Markdown;

import java.util.ArrayList;
import java.util.List;

public class Paragraph implements MarkdownObject {

    private final ArrayList<MarkdownObject> content;

    public Paragraph() {
        this.content = new ArrayList<>();
    }

    public Paragraph(List<MarkdownObject> list) {
        this.content = new ArrayList<>(list);
    }

    public ArrayList<MarkdownObject> getContent() {
        return content;
    }

    public void add(MarkdownObject object) {
        content.add(object);
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder();
        for (MarkdownObject markdown : content) {
            sb.append(markdown.render());
        }
        return sb.toString();
    }
}
