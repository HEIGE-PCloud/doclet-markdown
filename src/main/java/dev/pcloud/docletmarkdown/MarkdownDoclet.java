package dev.pcloud.docletmarkdown;

import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.util.DocTrees;
import dev.pcloud.docletmarkdown.Markdown.*;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.*;

import static dev.pcloud.docletmarkdown.FileUtils.createDirectory;
import static dev.pcloud.docletmarkdown.FileUtils.writeFile;
import static dev.pcloud.docletmarkdown.RenderUtils.printType;
import static dev.pcloud.docletmarkdown.RenderUtils.renderDocTree;

public class MarkdownDoclet implements Doclet {
    Reporter reporter;
    @Override
    public void init(Locale locale, Reporter reporter) {
        reporter.print(Diagnostic.Kind.NOTE, "Doclet using locale: " + locale);
        this.reporter = reporter;
    }



    // basically render a function to a string of its declaration
    private String renderExecutable(DocTrees trees, ExecutableElement e) {
        MarkdownPage page = new MarkdownPage();
        DocCommentTree docCommentTree = trees.getDocCommentTree(e);

        page.add(new Heading(4, "Type declaration"));
        page.add(new CodeBlock(e.toString(), "java"));
        if (docCommentTree != null) {
            page.add(2);
            for (DocTree dt : docCommentTree.getFirstSentence()) {
                page.add(renderDocTree(dt));
            }
        }
        page.add(2);

        var parameters = e.getParameters();
        if (!parameters.isEmpty()) {
            page.add(new Heading(4, "Parameters"));
            page.add();
            Table table = new Table(parameters.size() + 1, 3);
            table.setGrid(0, 0, "Name");
            table.setGrid(0, 1, "Type");
            table.setGrid(0, 2, "Description");
            int row = 1;

            for (var parameter : parameters) {
                table.setGrid(row, 0, new Code(parameter.toString()));
                table.setGrid(row, 1, new Code(printType(parameter.asType())));

                row++;
            }
            row = 1;
            if (docCommentTree != null) {
                for (var dt : docCommentTree.getBlockTags()) {
                    if (dt.getKind() == DocTree.Kind.PARAM) {
                        table.setGrid(row, 2, renderDocTree(dt));
                        row++;
                    }
                }
            }

            page.add(table);
            page.add(2);
        }
        page.add(new Heading(4, "Returns"));
        page.add();
        page.add(new Code(printType(e.getReturnType())));
        page.add(2);
        if (docCommentTree != null) {
            for (var dt : docCommentTree.getBlockTags()) {
                if (dt.getKind() == DocTree.Kind.RETURN) {
                    page.add(renderDocTree(dt));
                }
            }
        }
        page.add(2);
        return page.render(false);
    }

    private String render(String name, DocTrees trees, Elements utils, TypeElement t) {
        String interfaceName = t.getSimpleName().toString();
        MarkdownPage page = new MarkdownPage(interfaceName);
        page.add(new Heading(1, new Paragraph(
                List.of(
                        new Text(name + " "),
                        new Code(interfaceName)
                )
        )));
        page.add();

        if (trees.getDocCommentTree(t) != null) {
            page.add(renderDocTree(trees.getDocCommentTree(t)));
            page.add(2);
        }

        if (t.getEnclosedElements().stream().anyMatch(e -> e.getKind() == ElementKind.METHOD)) {
            page.add(new Heading(2, new Text("Methods")));
            page.add();

            for (Element e : t.getEnclosedElements()) {
                if (e.getKind() == ElementKind.METHOD) {
                    page.add(new Heading(3, new Code(e.getSimpleName().toString())));
                    page.add();
                    page.add(renderExecutable(trees, (ExecutableElement) e));
                }
            }
        }

        return page.render(true);
    }

    @Override
    public boolean run(DocletEnvironment docEnv) {
        // get the DocTrees utility class to access document comments
        Elements elementUtils = docEnv.getElementUtils();
        DocTrees docTrees = docEnv.getDocTrees();
        createDirectory("interfaces");
        createDirectory("classes");

        for (TypeElement t : ElementFilter.typesIn(docEnv.getIncludedElements())) {
             System.out.println(t.getKind() + ":" + t);
             if (!t.toString().startsWith(packageName)) {
                 continue;
             }
             if (t.toString().startsWith(excluded)) {
                 continue;
             }
             if (t.getKind() == ElementKind.INTERFACE) {
                 String res = render("Interface", docTrees, elementUtils, t);
                 writeFile("interfaces/" + t.getSimpleName() + ".md", res);
             } else if (t.getKind() == ElementKind.CLASS) {
                 String res = render("Class", docTrees, elementUtils, t);
                 writeFile("classes/" + t.getSimpleName() + ".md", res);
             }
        }
        return true;
    }

    @Override
    public String getName() {
        return "MarkdownDoclet";
    }

    private String packageName = "";
    private String excluded = "";

    @Override
    public Set<? extends Option> getSupportedOptions() {
        Option[] options = {
                new Option() {
                    @Override
                    public int getArgumentCount() {
                        return 1;
                    }

                    @Override
                    public String getDescription() {
                        return "Only generate docs for the specified package";
                    }

                    @Override
                    public Kind getKind() {
                        return Kind.STANDARD;
                    }

                    @Override
                    public List<String> getNames() {
                        return List.of(
                                "-packageName",
                                "--package-name",
                                "-p"
                        );
                    }

                    @Override
                    public String getParameters() {
                        return "";
                    }

                    @Override
                    public boolean process(String opt, List<String> arguments) {
                        packageName = arguments.get(0);
                        return true;
                    }
                },
                new Option() {
                    @Override
                    public int getArgumentCount() {
                        return 1;
                    }

                    @Override
                    public String getDescription() {
                        return "Exclude";
                    }

                    @Override
                    public Kind getKind() {
                        return Kind.STANDARD;
                    }

                    @Override
                    public List<String> getNames() {
                        return List.of("--exclude", "-exclude", "-e");
                    }

                    @Override
                    public String getParameters() {
                        return "";
                    }

                    @Override
                    public boolean process(String option, List<String> arguments) {
                        excluded = arguments.get(0);
                        return true;
                    }
                }
        };
        return new HashSet<>(Arrays.asList(options));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        // support the latest release
        return SourceVersion.latest();
    }
}