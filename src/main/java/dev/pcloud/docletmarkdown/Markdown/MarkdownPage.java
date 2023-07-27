package dev.pcloud.docletmarkdown.Markdown;

import java.util.ArrayList;

public class MarkdownPage {
    private final ArrayList<MarkdownObject> content = new ArrayList<>();
    private String title = "";

    public MarkdownPage() {}

    public MarkdownPage(String title) {
        this.title = title;
    }

    public void add(MarkdownObject markdown) {
        content.add(markdown);
    }

    public void add(String text) {
        content.add(new Text(text));
    }

    public void add() {
        content.add(new Newline());
    }

    public void add(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must > 0");
        }
        for (int i = 0; i < n; i++) {
            content.add(new Newline());
        }
    }
    public String render(boolean enableFrontMatter) {
        StringBuilder sb = new StringBuilder();
        if (enableFrontMatter) {
            sb.append("---\n");
            sb.append("title: ");
            sb.append(title);
            sb.append("\n");
            sb.append("---\n\n");
        }
        for (MarkdownObject markdown : content) {
            sb.append(markdown.render());
        }
        return sb.toString();
    }
}
