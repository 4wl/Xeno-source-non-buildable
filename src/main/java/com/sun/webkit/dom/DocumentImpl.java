/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.dom.AttrImpl;
import com.sun.webkit.dom.CDATASectionImpl;
import com.sun.webkit.dom.CSSStyleDeclarationImpl;
import com.sun.webkit.dom.CommentImpl;
import com.sun.webkit.dom.DOMImplementationImpl;
import com.sun.webkit.dom.DOMWindowImpl;
import com.sun.webkit.dom.DocumentFragmentImpl;
import com.sun.webkit.dom.DocumentTypeImpl;
import com.sun.webkit.dom.ElementImpl;
import com.sun.webkit.dom.EntityReferenceImpl;
import com.sun.webkit.dom.EventImpl;
import com.sun.webkit.dom.EventListenerImpl;
import com.sun.webkit.dom.HTMLCollectionImpl;
import com.sun.webkit.dom.HTMLElementImpl;
import com.sun.webkit.dom.HTMLHeadElementImpl;
import com.sun.webkit.dom.HTMLScriptElementImpl;
import com.sun.webkit.dom.NodeFilterImpl;
import com.sun.webkit.dom.NodeImpl;
import com.sun.webkit.dom.NodeIteratorImpl;
import com.sun.webkit.dom.NodeListImpl;
import com.sun.webkit.dom.ProcessingInstructionImpl;
import com.sun.webkit.dom.RangeImpl;
import com.sun.webkit.dom.StyleSheetListImpl;
import com.sun.webkit.dom.TextImpl;
import com.sun.webkit.dom.TreeWalkerImpl;
import com.sun.webkit.dom.XPathExpressionImpl;
import com.sun.webkit.dom.XPathNSResolverImpl;
import com.sun.webkit.dom.XPathResultImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.stylesheets.StyleSheetList;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathNSResolver;
import org.w3c.dom.xpath.XPathResult;

public class DocumentImpl
extends NodeImpl
implements Document,
XPathEvaluator,
DocumentView,
DocumentEvent {
    DocumentImpl(long l) {
        super(l);
    }

    static Document getImpl(long l) {
        return (Document)DocumentImpl.create(l);
    }

    static native boolean isHTMLDocumentImpl(long var0);

    @Override
    public Object evaluate(String string, Node node, XPathNSResolver xPathNSResolver, short s, Object object) throws DOMException {
        return this.evaluate(string, node, xPathNSResolver, s, (XPathResult)object);
    }

    @Override
    public DocumentType getDoctype() {
        return DocumentTypeImpl.getImpl(DocumentImpl.getDoctypeImpl(this.getPeer()));
    }

    static native long getDoctypeImpl(long var0);

    @Override
    public DOMImplementation getImplementation() {
        return DOMImplementationImpl.getImpl(DocumentImpl.getImplementationImpl(this.getPeer()));
    }

    static native long getImplementationImpl(long var0);

    @Override
    public Element getDocumentElement() {
        return ElementImpl.getImpl(DocumentImpl.getDocumentElementImpl(this.getPeer()));
    }

    static native long getDocumentElementImpl(long var0);

    @Override
    public String getInputEncoding() {
        return DocumentImpl.getInputEncodingImpl(this.getPeer());
    }

    static native String getInputEncodingImpl(long var0);

    @Override
    public String getXmlEncoding() {
        return DocumentImpl.getXmlEncodingImpl(this.getPeer());
    }

    static native String getXmlEncodingImpl(long var0);

    @Override
    public String getXmlVersion() {
        return DocumentImpl.getXmlVersionImpl(this.getPeer());
    }

    static native String getXmlVersionImpl(long var0);

    @Override
    public void setXmlVersion(String string) throws DOMException {
        DocumentImpl.setXmlVersionImpl(this.getPeer(), string);
    }

    static native void setXmlVersionImpl(long var0, String var2);

    @Override
    public boolean getXmlStandalone() {
        return DocumentImpl.getXmlStandaloneImpl(this.getPeer());
    }

    static native boolean getXmlStandaloneImpl(long var0);

    @Override
    public void setXmlStandalone(boolean bl) throws DOMException {
        DocumentImpl.setXmlStandaloneImpl(this.getPeer(), bl);
    }

    static native void setXmlStandaloneImpl(long var0, boolean var2);

    @Override
    public String getDocumentURI() {
        return DocumentImpl.getDocumentURIImpl(this.getPeer());
    }

    static native String getDocumentURIImpl(long var0);

    @Override
    public AbstractView getDefaultView() {
        return DOMWindowImpl.getImpl(DocumentImpl.getDefaultViewImpl(this.getPeer()));
    }

    static native long getDefaultViewImpl(long var0);

    public StyleSheetList getStyleSheets() {
        return StyleSheetListImpl.getImpl(DocumentImpl.getStyleSheetsImpl(this.getPeer()));
    }

    static native long getStyleSheetsImpl(long var0);

    public String getTitle() {
        return DocumentImpl.getTitleImpl(this.getPeer());
    }

    static native String getTitleImpl(long var0);

    public void setTitle(String string) {
        DocumentImpl.setTitleImpl(this.getPeer(), string);
    }

    static native void setTitleImpl(long var0, String var2);

    public String getReferrer() {
        return DocumentImpl.getReferrerImpl(this.getPeer());
    }

    static native String getReferrerImpl(long var0);

    public String getDomain() {
        return DocumentImpl.getDomainImpl(this.getPeer());
    }

    static native String getDomainImpl(long var0);

    public String getURL() {
        return DocumentImpl.getURLImpl(this.getPeer());
    }

    static native String getURLImpl(long var0);

    public String getCookie() throws DOMException {
        return DocumentImpl.getCookieImpl(this.getPeer());
    }

    static native String getCookieImpl(long var0);

    public void setCookie(String string) throws DOMException {
        DocumentImpl.setCookieImpl(this.getPeer(), string);
    }

    static native void setCookieImpl(long var0, String var2);

    public HTMLElement getBody() {
        return HTMLElementImpl.getImpl(DocumentImpl.getBodyImpl(this.getPeer()));
    }

    static native long getBodyImpl(long var0);

    public void setBody(HTMLElement hTMLElement) throws DOMException {
        DocumentImpl.setBodyImpl(this.getPeer(), HTMLElementImpl.getPeer(hTMLElement));
    }

    static native void setBodyImpl(long var0, long var2);

    public HTMLHeadElement getHead() {
        return HTMLHeadElementImpl.getImpl(DocumentImpl.getHeadImpl(this.getPeer()));
    }

    static native long getHeadImpl(long var0);

    public HTMLCollection getImages() {
        return HTMLCollectionImpl.getImpl(DocumentImpl.getImagesImpl(this.getPeer()));
    }

    static native long getImagesImpl(long var0);

    public HTMLCollection getApplets() {
        return HTMLCollectionImpl.getImpl(DocumentImpl.getAppletsImpl(this.getPeer()));
    }

    static native long getAppletsImpl(long var0);

    public HTMLCollection getLinks() {
        return HTMLCollectionImpl.getImpl(DocumentImpl.getLinksImpl(this.getPeer()));
    }

    static native long getLinksImpl(long var0);

    public HTMLCollection getForms() {
        return HTMLCollectionImpl.getImpl(DocumentImpl.getFormsImpl(this.getPeer()));
    }

    static native long getFormsImpl(long var0);

    public HTMLCollection getAnchors() {
        return HTMLCollectionImpl.getImpl(DocumentImpl.getAnchorsImpl(this.getPeer()));
    }

    static native long getAnchorsImpl(long var0);

    public String getLastModified() {
        return DocumentImpl.getLastModifiedImpl(this.getPeer());
    }

    static native String getLastModifiedImpl(long var0);

    public String getCharset() {
        return DocumentImpl.getCharsetImpl(this.getPeer());
    }

    static native String getCharsetImpl(long var0);

    public void setCharset(String string) {
        DocumentImpl.setCharsetImpl(this.getPeer(), string);
    }

    static native void setCharsetImpl(long var0, String var2);

    public String getDefaultCharset() {
        return DocumentImpl.getDefaultCharsetImpl(this.getPeer());
    }

    static native String getDefaultCharsetImpl(long var0);

    public String getReadyState() {
        return DocumentImpl.getReadyStateImpl(this.getPeer());
    }

    static native String getReadyStateImpl(long var0);

    public String getCharacterSet() {
        return DocumentImpl.getCharacterSetImpl(this.getPeer());
    }

    static native String getCharacterSetImpl(long var0);

    public String getPreferredStylesheetSet() {
        return DocumentImpl.getPreferredStylesheetSetImpl(this.getPeer());
    }

    static native String getPreferredStylesheetSetImpl(long var0);

    public String getSelectedStylesheetSet() {
        return DocumentImpl.getSelectedStylesheetSetImpl(this.getPeer());
    }

    static native String getSelectedStylesheetSetImpl(long var0);

    public void setSelectedStylesheetSet(String string) {
        DocumentImpl.setSelectedStylesheetSetImpl(this.getPeer(), string);
    }

    static native void setSelectedStylesheetSetImpl(long var0, String var2);

    public String getCompatMode() {
        return DocumentImpl.getCompatModeImpl(this.getPeer());
    }

    static native String getCompatModeImpl(long var0);

    public boolean getWebkitIsFullScreen() {
        return DocumentImpl.getWebkitIsFullScreenImpl(this.getPeer());
    }

    static native boolean getWebkitIsFullScreenImpl(long var0);

    public boolean getWebkitFullScreenKeyboardInputAllowed() {
        return DocumentImpl.getWebkitFullScreenKeyboardInputAllowedImpl(this.getPeer());
    }

    static native boolean getWebkitFullScreenKeyboardInputAllowedImpl(long var0);

    public Element getWebkitCurrentFullScreenElement() {
        return ElementImpl.getImpl(DocumentImpl.getWebkitCurrentFullScreenElementImpl(this.getPeer()));
    }

    static native long getWebkitCurrentFullScreenElementImpl(long var0);

    public boolean getWebkitFullscreenEnabled() {
        return DocumentImpl.getWebkitFullscreenEnabledImpl(this.getPeer());
    }

    static native boolean getWebkitFullscreenEnabledImpl(long var0);

    public Element getWebkitFullscreenElement() {
        return ElementImpl.getImpl(DocumentImpl.getWebkitFullscreenElementImpl(this.getPeer()));
    }

    static native long getWebkitFullscreenElementImpl(long var0);

    public EventListener getOnabort() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnabortImpl(this.getPeer()));
    }

    static native long getOnabortImpl(long var0);

    public void setOnabort(EventListener eventListener) {
        DocumentImpl.setOnabortImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnabortImpl(long var0, long var2);

    public EventListener getOnblur() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnblurImpl(this.getPeer()));
    }

    static native long getOnblurImpl(long var0);

    public void setOnblur(EventListener eventListener) {
        DocumentImpl.setOnblurImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnblurImpl(long var0, long var2);

    public EventListener getOnchange() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnchangeImpl(this.getPeer()));
    }

    static native long getOnchangeImpl(long var0);

    public void setOnchange(EventListener eventListener) {
        DocumentImpl.setOnchangeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnchangeImpl(long var0, long var2);

    public EventListener getOnclick() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnclickImpl(this.getPeer()));
    }

    static native long getOnclickImpl(long var0);

    public void setOnclick(EventListener eventListener) {
        DocumentImpl.setOnclickImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnclickImpl(long var0, long var2);

    public EventListener getOncontextmenu() {
        return EventListenerImpl.getImpl(DocumentImpl.getOncontextmenuImpl(this.getPeer()));
    }

    static native long getOncontextmenuImpl(long var0);

    public void setOncontextmenu(EventListener eventListener) {
        DocumentImpl.setOncontextmenuImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOncontextmenuImpl(long var0, long var2);

    public EventListener getOndblclick() {
        return EventListenerImpl.getImpl(DocumentImpl.getOndblclickImpl(this.getPeer()));
    }

    static native long getOndblclickImpl(long var0);

    public void setOndblclick(EventListener eventListener) {
        DocumentImpl.setOndblclickImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndblclickImpl(long var0, long var2);

    public EventListener getOndrag() {
        return EventListenerImpl.getImpl(DocumentImpl.getOndragImpl(this.getPeer()));
    }

    static native long getOndragImpl(long var0);

    public void setOndrag(EventListener eventListener) {
        DocumentImpl.setOndragImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragImpl(long var0, long var2);

    public EventListener getOndragend() {
        return EventListenerImpl.getImpl(DocumentImpl.getOndragendImpl(this.getPeer()));
    }

    static native long getOndragendImpl(long var0);

    public void setOndragend(EventListener eventListener) {
        DocumentImpl.setOndragendImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragendImpl(long var0, long var2);

    public EventListener getOndragenter() {
        return EventListenerImpl.getImpl(DocumentImpl.getOndragenterImpl(this.getPeer()));
    }

    static native long getOndragenterImpl(long var0);

    public void setOndragenter(EventListener eventListener) {
        DocumentImpl.setOndragenterImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragenterImpl(long var0, long var2);

    public EventListener getOndragleave() {
        return EventListenerImpl.getImpl(DocumentImpl.getOndragleaveImpl(this.getPeer()));
    }

    static native long getOndragleaveImpl(long var0);

    public void setOndragleave(EventListener eventListener) {
        DocumentImpl.setOndragleaveImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragleaveImpl(long var0, long var2);

    public EventListener getOndragover() {
        return EventListenerImpl.getImpl(DocumentImpl.getOndragoverImpl(this.getPeer()));
    }

    static native long getOndragoverImpl(long var0);

    public void setOndragover(EventListener eventListener) {
        DocumentImpl.setOndragoverImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragoverImpl(long var0, long var2);

    public EventListener getOndragstart() {
        return EventListenerImpl.getImpl(DocumentImpl.getOndragstartImpl(this.getPeer()));
    }

    static native long getOndragstartImpl(long var0);

    public void setOndragstart(EventListener eventListener) {
        DocumentImpl.setOndragstartImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndragstartImpl(long var0, long var2);

    public EventListener getOndrop() {
        return EventListenerImpl.getImpl(DocumentImpl.getOndropImpl(this.getPeer()));
    }

    static native long getOndropImpl(long var0);

    public void setOndrop(EventListener eventListener) {
        DocumentImpl.setOndropImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOndropImpl(long var0, long var2);

    public EventListener getOnerror() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnerrorImpl(this.getPeer()));
    }

    static native long getOnerrorImpl(long var0);

    public void setOnerror(EventListener eventListener) {
        DocumentImpl.setOnerrorImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnerrorImpl(long var0, long var2);

    public EventListener getOnfocus() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnfocusImpl(this.getPeer()));
    }

    static native long getOnfocusImpl(long var0);

    public void setOnfocus(EventListener eventListener) {
        DocumentImpl.setOnfocusImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnfocusImpl(long var0, long var2);

    public EventListener getOninput() {
        return EventListenerImpl.getImpl(DocumentImpl.getOninputImpl(this.getPeer()));
    }

    static native long getOninputImpl(long var0);

    public void setOninput(EventListener eventListener) {
        DocumentImpl.setOninputImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOninputImpl(long var0, long var2);

    public EventListener getOninvalid() {
        return EventListenerImpl.getImpl(DocumentImpl.getOninvalidImpl(this.getPeer()));
    }

    static native long getOninvalidImpl(long var0);

    public void setOninvalid(EventListener eventListener) {
        DocumentImpl.setOninvalidImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOninvalidImpl(long var0, long var2);

    public EventListener getOnkeydown() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnkeydownImpl(this.getPeer()));
    }

    static native long getOnkeydownImpl(long var0);

    public void setOnkeydown(EventListener eventListener) {
        DocumentImpl.setOnkeydownImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnkeydownImpl(long var0, long var2);

    public EventListener getOnkeypress() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnkeypressImpl(this.getPeer()));
    }

    static native long getOnkeypressImpl(long var0);

    public void setOnkeypress(EventListener eventListener) {
        DocumentImpl.setOnkeypressImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnkeypressImpl(long var0, long var2);

    public EventListener getOnkeyup() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnkeyupImpl(this.getPeer()));
    }

    static native long getOnkeyupImpl(long var0);

    public void setOnkeyup(EventListener eventListener) {
        DocumentImpl.setOnkeyupImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnkeyupImpl(long var0, long var2);

    public EventListener getOnload() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnloadImpl(this.getPeer()));
    }

    static native long getOnloadImpl(long var0);

    public void setOnload(EventListener eventListener) {
        DocumentImpl.setOnloadImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnloadImpl(long var0, long var2);

    public EventListener getOnmousedown() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnmousedownImpl(this.getPeer()));
    }

    static native long getOnmousedownImpl(long var0);

    public void setOnmousedown(EventListener eventListener) {
        DocumentImpl.setOnmousedownImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmousedownImpl(long var0, long var2);

    public EventListener getOnmouseenter() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnmouseenterImpl(this.getPeer()));
    }

    static native long getOnmouseenterImpl(long var0);

    public void setOnmouseenter(EventListener eventListener) {
        DocumentImpl.setOnmouseenterImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmouseenterImpl(long var0, long var2);

    public EventListener getOnmouseleave() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnmouseleaveImpl(this.getPeer()));
    }

    static native long getOnmouseleaveImpl(long var0);

    public void setOnmouseleave(EventListener eventListener) {
        DocumentImpl.setOnmouseleaveImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmouseleaveImpl(long var0, long var2);

    public EventListener getOnmousemove() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnmousemoveImpl(this.getPeer()));
    }

    static native long getOnmousemoveImpl(long var0);

    public void setOnmousemove(EventListener eventListener) {
        DocumentImpl.setOnmousemoveImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmousemoveImpl(long var0, long var2);

    public EventListener getOnmouseout() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnmouseoutImpl(this.getPeer()));
    }

    static native long getOnmouseoutImpl(long var0);

    public void setOnmouseout(EventListener eventListener) {
        DocumentImpl.setOnmouseoutImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmouseoutImpl(long var0, long var2);

    public EventListener getOnmouseover() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnmouseoverImpl(this.getPeer()));
    }

    static native long getOnmouseoverImpl(long var0);

    public void setOnmouseover(EventListener eventListener) {
        DocumentImpl.setOnmouseoverImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmouseoverImpl(long var0, long var2);

    public EventListener getOnmouseup() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnmouseupImpl(this.getPeer()));
    }

    static native long getOnmouseupImpl(long var0);

    public void setOnmouseup(EventListener eventListener) {
        DocumentImpl.setOnmouseupImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmouseupImpl(long var0, long var2);

    public EventListener getOnmousewheel() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnmousewheelImpl(this.getPeer()));
    }

    static native long getOnmousewheelImpl(long var0);

    public void setOnmousewheel(EventListener eventListener) {
        DocumentImpl.setOnmousewheelImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnmousewheelImpl(long var0, long var2);

    public EventListener getOnreadystatechange() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnreadystatechangeImpl(this.getPeer()));
    }

    static native long getOnreadystatechangeImpl(long var0);

    public void setOnreadystatechange(EventListener eventListener) {
        DocumentImpl.setOnreadystatechangeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnreadystatechangeImpl(long var0, long var2);

    public EventListener getOnscroll() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnscrollImpl(this.getPeer()));
    }

    static native long getOnscrollImpl(long var0);

    public void setOnscroll(EventListener eventListener) {
        DocumentImpl.setOnscrollImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnscrollImpl(long var0, long var2);

    public EventListener getOnselect() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnselectImpl(this.getPeer()));
    }

    static native long getOnselectImpl(long var0);

    public void setOnselect(EventListener eventListener) {
        DocumentImpl.setOnselectImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnselectImpl(long var0, long var2);

    public EventListener getOnsubmit() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnsubmitImpl(this.getPeer()));
    }

    static native long getOnsubmitImpl(long var0);

    public void setOnsubmit(EventListener eventListener) {
        DocumentImpl.setOnsubmitImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnsubmitImpl(long var0, long var2);

    public EventListener getOnwheel() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnwheelImpl(this.getPeer()));
    }

    static native long getOnwheelImpl(long var0);

    public void setOnwheel(EventListener eventListener) {
        DocumentImpl.setOnwheelImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnwheelImpl(long var0, long var2);

    public EventListener getOnbeforecut() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnbeforecutImpl(this.getPeer()));
    }

    static native long getOnbeforecutImpl(long var0);

    public void setOnbeforecut(EventListener eventListener) {
        DocumentImpl.setOnbeforecutImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnbeforecutImpl(long var0, long var2);

    public EventListener getOncut() {
        return EventListenerImpl.getImpl(DocumentImpl.getOncutImpl(this.getPeer()));
    }

    static native long getOncutImpl(long var0);

    public void setOncut(EventListener eventListener) {
        DocumentImpl.setOncutImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOncutImpl(long var0, long var2);

    public EventListener getOnbeforecopy() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnbeforecopyImpl(this.getPeer()));
    }

    static native long getOnbeforecopyImpl(long var0);

    public void setOnbeforecopy(EventListener eventListener) {
        DocumentImpl.setOnbeforecopyImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnbeforecopyImpl(long var0, long var2);

    public EventListener getOncopy() {
        return EventListenerImpl.getImpl(DocumentImpl.getOncopyImpl(this.getPeer()));
    }

    static native long getOncopyImpl(long var0);

    public void setOncopy(EventListener eventListener) {
        DocumentImpl.setOncopyImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOncopyImpl(long var0, long var2);

    public EventListener getOnbeforepaste() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnbeforepasteImpl(this.getPeer()));
    }

    static native long getOnbeforepasteImpl(long var0);

    public void setOnbeforepaste(EventListener eventListener) {
        DocumentImpl.setOnbeforepasteImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnbeforepasteImpl(long var0, long var2);

    public EventListener getOnpaste() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnpasteImpl(this.getPeer()));
    }

    static native long getOnpasteImpl(long var0);

    public void setOnpaste(EventListener eventListener) {
        DocumentImpl.setOnpasteImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnpasteImpl(long var0, long var2);

    public EventListener getOnreset() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnresetImpl(this.getPeer()));
    }

    static native long getOnresetImpl(long var0);

    public void setOnreset(EventListener eventListener) {
        DocumentImpl.setOnresetImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnresetImpl(long var0, long var2);

    public EventListener getOnsearch() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnsearchImpl(this.getPeer()));
    }

    static native long getOnsearchImpl(long var0);

    public void setOnsearch(EventListener eventListener) {
        DocumentImpl.setOnsearchImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnsearchImpl(long var0, long var2);

    public EventListener getOnselectstart() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnselectstartImpl(this.getPeer()));
    }

    static native long getOnselectstartImpl(long var0);

    public void setOnselectstart(EventListener eventListener) {
        DocumentImpl.setOnselectstartImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnselectstartImpl(long var0, long var2);

    public EventListener getOnselectionchange() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnselectionchangeImpl(this.getPeer()));
    }

    static native long getOnselectionchangeImpl(long var0);

    public void setOnselectionchange(EventListener eventListener) {
        DocumentImpl.setOnselectionchangeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnselectionchangeImpl(long var0, long var2);

    public EventListener getOnwebkitfullscreenchange() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnwebkitfullscreenchangeImpl(this.getPeer()));
    }

    static native long getOnwebkitfullscreenchangeImpl(long var0);

    public void setOnwebkitfullscreenchange(EventListener eventListener) {
        DocumentImpl.setOnwebkitfullscreenchangeImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnwebkitfullscreenchangeImpl(long var0, long var2);

    public EventListener getOnwebkitfullscreenerror() {
        return EventListenerImpl.getImpl(DocumentImpl.getOnwebkitfullscreenerrorImpl(this.getPeer()));
    }

    static native long getOnwebkitfullscreenerrorImpl(long var0);

    public void setOnwebkitfullscreenerror(EventListener eventListener) {
        DocumentImpl.setOnwebkitfullscreenerrorImpl(this.getPeer(), EventListenerImpl.getPeer(eventListener));
    }

    static native void setOnwebkitfullscreenerrorImpl(long var0, long var2);

    public String getVisibilityState() {
        return DocumentImpl.getVisibilityStateImpl(this.getPeer());
    }

    static native String getVisibilityStateImpl(long var0);

    public boolean getHidden() {
        return DocumentImpl.getHiddenImpl(this.getPeer());
    }

    static native boolean getHiddenImpl(long var0);

    public HTMLScriptElement getCurrentScript() {
        return HTMLScriptElementImpl.getImpl(DocumentImpl.getCurrentScriptImpl(this.getPeer()));
    }

    static native long getCurrentScriptImpl(long var0);

    public String getOrigin() {
        return DocumentImpl.getOriginImpl(this.getPeer());
    }

    static native String getOriginImpl(long var0);

    @Override
    public Element createElement(String string) throws DOMException {
        return ElementImpl.getImpl(DocumentImpl.createElementImpl(this.getPeer(), string));
    }

    static native long createElementImpl(long var0, String var2);

    @Override
    public DocumentFragment createDocumentFragment() {
        return DocumentFragmentImpl.getImpl(DocumentImpl.createDocumentFragmentImpl(this.getPeer()));
    }

    static native long createDocumentFragmentImpl(long var0);

    @Override
    public Text createTextNode(String string) {
        return TextImpl.getImpl(DocumentImpl.createTextNodeImpl(this.getPeer(), string));
    }

    static native long createTextNodeImpl(long var0, String var2);

    @Override
    public Comment createComment(String string) {
        return CommentImpl.getImpl(DocumentImpl.createCommentImpl(this.getPeer(), string));
    }

    static native long createCommentImpl(long var0, String var2);

    @Override
    public CDATASection createCDATASection(String string) throws DOMException {
        return CDATASectionImpl.getImpl(DocumentImpl.createCDATASectionImpl(this.getPeer(), string));
    }

    static native long createCDATASectionImpl(long var0, String var2);

    @Override
    public ProcessingInstruction createProcessingInstruction(String string, String string2) throws DOMException {
        return (ProcessingInstruction)ProcessingInstructionImpl.getImpl(DocumentImpl.createProcessingInstructionImpl(this.getPeer(), string, string2));
    }

    static native long createProcessingInstructionImpl(long var0, String var2, String var3);

    @Override
    public Attr createAttribute(String string) throws DOMException {
        return AttrImpl.getImpl(DocumentImpl.createAttributeImpl(this.getPeer(), string));
    }

    static native long createAttributeImpl(long var0, String var2);

    @Override
    public EntityReference createEntityReference(String string) throws DOMException {
        return EntityReferenceImpl.getImpl(DocumentImpl.createEntityReferenceImpl(this.getPeer(), string));
    }

    static native long createEntityReferenceImpl(long var0, String var2);

    @Override
    public NodeList getElementsByTagName(String string) {
        return NodeListImpl.getImpl(DocumentImpl.getElementsByTagNameImpl(this.getPeer(), string));
    }

    static native long getElementsByTagNameImpl(long var0, String var2);

    @Override
    public Node importNode(Node node, boolean bl) throws DOMException {
        return NodeImpl.getImpl(DocumentImpl.importNodeImpl(this.getPeer(), NodeImpl.getPeer(node), bl));
    }

    static native long importNodeImpl(long var0, long var2, boolean var4);

    @Override
    public Element createElementNS(String string, String string2) throws DOMException {
        return ElementImpl.getImpl(DocumentImpl.createElementNSImpl(this.getPeer(), string, string2));
    }

    static native long createElementNSImpl(long var0, String var2, String var3);

    @Override
    public Attr createAttributeNS(String string, String string2) throws DOMException {
        return AttrImpl.getImpl(DocumentImpl.createAttributeNSImpl(this.getPeer(), string, string2));
    }

    static native long createAttributeNSImpl(long var0, String var2, String var3);

    @Override
    public NodeList getElementsByTagNameNS(String string, String string2) {
        return NodeListImpl.getImpl(DocumentImpl.getElementsByTagNameNSImpl(this.getPeer(), string, string2));
    }

    static native long getElementsByTagNameNSImpl(long var0, String var2, String var3);

    @Override
    public Element getElementById(String string) {
        return ElementImpl.getImpl(DocumentImpl.getElementByIdImpl(this.getPeer(), string));
    }

    static native long getElementByIdImpl(long var0, String var2);

    @Override
    public Node adoptNode(Node node) throws DOMException {
        return NodeImpl.getImpl(DocumentImpl.adoptNodeImpl(this.getPeer(), NodeImpl.getPeer(node)));
    }

    static native long adoptNodeImpl(long var0, long var2);

    @Override
    public Event createEvent(String string) throws DOMException {
        return EventImpl.getImpl(DocumentImpl.createEventImpl(this.getPeer(), string));
    }

    static native long createEventImpl(long var0, String var2);

    public Range createRange() {
        return RangeImpl.getImpl(DocumentImpl.createRangeImpl(this.getPeer()));
    }

    static native long createRangeImpl(long var0);

    public NodeIterator createNodeIterator(Node node, int n, NodeFilter nodeFilter, boolean bl) throws DOMException {
        return NodeIteratorImpl.getImpl(DocumentImpl.createNodeIteratorImpl(this.getPeer(), NodeImpl.getPeer(node), n, NodeFilterImpl.getPeer(nodeFilter), bl));
    }

    static native long createNodeIteratorImpl(long var0, long var2, int var4, long var5, boolean var7);

    public TreeWalker createTreeWalker(Node node, int n, NodeFilter nodeFilter, boolean bl) throws DOMException {
        return TreeWalkerImpl.getImpl(DocumentImpl.createTreeWalkerImpl(this.getPeer(), NodeImpl.getPeer(node), n, NodeFilterImpl.getPeer(nodeFilter), bl));
    }

    static native long createTreeWalkerImpl(long var0, long var2, int var4, long var5, boolean var7);

    public CSSStyleDeclaration getOverrideStyle(Element element, String string) {
        return CSSStyleDeclarationImpl.getImpl(DocumentImpl.getOverrideStyleImpl(this.getPeer(), ElementImpl.getPeer(element), string));
    }

    static native long getOverrideStyleImpl(long var0, long var2, String var4);

    @Override
    public XPathExpression createExpression(String string, XPathNSResolver xPathNSResolver) throws DOMException {
        return XPathExpressionImpl.getImpl(DocumentImpl.createExpressionImpl(this.getPeer(), string, XPathNSResolverImpl.getPeer(xPathNSResolver)));
    }

    static native long createExpressionImpl(long var0, String var2, long var3);

    @Override
    public XPathNSResolver createNSResolver(Node node) {
        return XPathNSResolverImpl.getImpl(DocumentImpl.createNSResolverImpl(this.getPeer(), NodeImpl.getPeer(node)));
    }

    static native long createNSResolverImpl(long var0, long var2);

    public XPathResult evaluate(String string, Node node, XPathNSResolver xPathNSResolver, short s, XPathResult xPathResult) throws DOMException {
        return XPathResultImpl.getImpl(DocumentImpl.evaluateImpl(this.getPeer(), string, NodeImpl.getPeer(node), XPathNSResolverImpl.getPeer(xPathNSResolver), s, XPathResultImpl.getPeer(xPathResult)));
    }

    static native long evaluateImpl(long var0, String var2, long var3, long var5, short var7, long var8);

    public boolean execCommand(String string, boolean bl, String string2) {
        return DocumentImpl.execCommandImpl(this.getPeer(), string, bl, string2);
    }

    static native boolean execCommandImpl(long var0, String var2, boolean var3, String var4);

    public boolean queryCommandEnabled(String string) {
        return DocumentImpl.queryCommandEnabledImpl(this.getPeer(), string);
    }

    static native boolean queryCommandEnabledImpl(long var0, String var2);

    public boolean queryCommandIndeterm(String string) {
        return DocumentImpl.queryCommandIndetermImpl(this.getPeer(), string);
    }

    static native boolean queryCommandIndetermImpl(long var0, String var2);

    public boolean queryCommandState(String string) {
        return DocumentImpl.queryCommandStateImpl(this.getPeer(), string);
    }

    static native boolean queryCommandStateImpl(long var0, String var2);

    public boolean queryCommandSupported(String string) {
        return DocumentImpl.queryCommandSupportedImpl(this.getPeer(), string);
    }

    static native boolean queryCommandSupportedImpl(long var0, String var2);

    public String queryCommandValue(String string) {
        return DocumentImpl.queryCommandValueImpl(this.getPeer(), string);
    }

    static native String queryCommandValueImpl(long var0, String var2);

    public NodeList getElementsByName(String string) {
        return NodeListImpl.getImpl(DocumentImpl.getElementsByNameImpl(this.getPeer(), string));
    }

    static native long getElementsByNameImpl(long var0, String var2);

    public Element elementFromPoint(int n, int n2) {
        return ElementImpl.getImpl(DocumentImpl.elementFromPointImpl(this.getPeer(), n, n2));
    }

    static native long elementFromPointImpl(long var0, int var2, int var3);

    public Range caretRangeFromPoint(int n, int n2) {
        return RangeImpl.getImpl(DocumentImpl.caretRangeFromPointImpl(this.getPeer(), n, n2));
    }

    static native long caretRangeFromPointImpl(long var0, int var2, int var3);

    public CSSStyleDeclaration createCSSStyleDeclaration() {
        return CSSStyleDeclarationImpl.getImpl(DocumentImpl.createCSSStyleDeclarationImpl(this.getPeer()));
    }

    static native long createCSSStyleDeclarationImpl(long var0);

    public NodeList getElementsByClassName(String string) {
        return NodeListImpl.getImpl(DocumentImpl.getElementsByClassNameImpl(this.getPeer(), string));
    }

    static native long getElementsByClassNameImpl(long var0, String var2);

    public Element querySelector(String string) throws DOMException {
        return ElementImpl.getImpl(DocumentImpl.querySelectorImpl(this.getPeer(), string));
    }

    static native long querySelectorImpl(long var0, String var2);

    public NodeList querySelectorAll(String string) throws DOMException {
        return NodeListImpl.getImpl(DocumentImpl.querySelectorAllImpl(this.getPeer(), string));
    }

    static native long querySelectorAllImpl(long var0, String var2);

    public void webkitCancelFullScreen() {
        DocumentImpl.webkitCancelFullScreenImpl(this.getPeer());
    }

    static native void webkitCancelFullScreenImpl(long var0);

    public void webkitExitFullscreen() {
        DocumentImpl.webkitExitFullscreenImpl(this.getPeer());
    }

    static native void webkitExitFullscreenImpl(long var0);

    @Override
    public boolean getStrictErrorChecking() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setStrictErrorChecking(boolean bl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Node renameNode(Node node, String string, String string2) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DOMConfiguration getDomConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void normalizeDocument() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDocumentURI(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

