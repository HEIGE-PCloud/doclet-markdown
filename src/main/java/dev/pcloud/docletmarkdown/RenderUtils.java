package dev.pcloud.docletmarkdown;

import com.sun.source.doctree.*;
import dev.pcloud.docletmarkdown.Markdown.*;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;


public class RenderUtils {

    public static String printType(TypeMirror type) {
        switch (type.getKind()) {
            case BOOLEAN:
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case CHAR:
            case FLOAT:
            case DOUBLE:
            case VOID:
            case NONE:
            case NULL:
            case ARRAY:
                return type.toString();
            case DECLARED:
                DeclaredType declaredType = (DeclaredType) type;
                Element element = declaredType.asElement();
                var typeArguments = declaredType.getTypeArguments();
                StringBuilder sb = new StringBuilder();
                sb.append(element.getSimpleName().toString());
                if (!typeArguments.isEmpty()) {
                    sb.append('<');
                    for (var typeArgument : typeArguments) {
                        sb.append(printType(typeArgument));
                        sb.append(',');
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append('>');
                }
                return sb.toString();
            case ERROR:
            case PACKAGE:
            case EXECUTABLE:
            case OTHER:
            case UNION:
            case INTERSECTION:
            case MODULE:
            case TYPEVAR:
                break;
            case WILDCARD:
                WildcardType wildcardType = (WildcardType) type;
                TypeMirror superType = wildcardType.getSuperBound();
                if (superType != null) {
                    return "? super " + printType(superType);
                }
                TypeMirror extendsType = wildcardType.getExtendsBound();
                if (extendsType != null) {
                    return "? extends " + printType(extendsType);
                }
                break;
        }
        return type.toString();
    }
    public static MarkdownObject renderDocTree(DocTree dt) {
        switch (dt.getKind()) {
            case ATTRIBUTE:
                break;
            case AUTHOR:
                break;
            case CODE: {
                LiteralTree lt = (LiteralTree) dt;
                return new Code(lt.getBody().getBody());
            }
            case COMMENT:
                break;
            case DEPRECATED:
                break;
            case DOC_COMMENT:
                break;
            case DOC_ROOT:
                break;
            case DOC_TYPE:
                break;
            case END_ELEMENT:
                break;
            case ENTITY:
                break;
            case ERRONEOUS:
                break;
            case EXCEPTION:
                break;
            case HIDDEN:
                break;
            case IDENTIFIER:
                break;
            case INDEX:
                break;
            case INHERIT_DOC:
                break;
            case LINK: {
                LinkTree lt = (LinkTree) dt;
                ReferenceTree rt = lt.getReference();
                return new Link(rt.toString(), rt.toString());
            }
            case LINK_PLAIN:
                break;
            case LITERAL:
                break;
            case PARAM: {
                ParamTree pt = (ParamTree) dt;
                Paragraph paragraph = new Paragraph();
                for (var d : pt.getDescription()) {
                    paragraph.add(renderDocTree(d));
                }
                return paragraph;
            }
            case PROVIDES:
                break;
            case REFERENCE:
                break;
            case RETURN: {
                ReturnTree rt = (ReturnTree) dt;
                Paragraph paragraph = new Paragraph();
                for (var t : rt.getDescription()) {
                    paragraph.add(renderDocTree(t));
                }
                return paragraph;
            }
            case SEE:
                break;
            case SERIAL:
                break;
            case SERIAL_DATA:
                break;
            case SERIAL_FIELD:
                break;
            case SINCE:
                break;
            case START_ELEMENT:
                break;
            case SUMMARY:
                break;
            case TEXT:
                break;
            case THROWS:
                break;
            case UNKNOWN_BLOCK_TAG:
                break;
            case UNKNOWN_INLINE_TAG:
                break;
            case USES:
                break;
            case VALUE:
                break;
            case VERSION:
                break;
            case OTHER:
                break;
        }
        return new Text(dt.toString());
    }
}
