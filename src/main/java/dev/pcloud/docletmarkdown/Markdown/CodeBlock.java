package dev.pcloud.docletmarkdown.Markdown;

public class CodeBlock implements MarkdownObject {
    private String code;
    private String lang;
    public CodeBlock(String code, String lang) {
        this.code = code;
        this.lang = lang;
    }

    @Override
    public String render() {
        return "```" + lang + "\n" + code + "\n" + "```";
    }
}
