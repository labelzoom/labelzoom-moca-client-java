package com.labelzoom.moca;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

class HttpMocaRequest extends MocaRequest
{
    public HttpMocaRequest(final String command) { super(command); }
    public HttpMocaRequest(final String command, final boolean autoCommit) { super(command, autoCommit); }

    @Override
    public String toString()
    {
        try
        {
            final DocumentBuilderFactory documentFactor = DocumentBuilderFactory.newInstance();
            final DocumentBuilder documentBuilder = documentFactor.newDocumentBuilder();
            final Document document = documentBuilder.newDocument();

            // Root element
            final Element root = document.createElement("moca-request");
            root.setAttribute("autocommit", autoCommit ? "True" : "False");
            document.appendChild(root);

            // Environment node
            if (this.environment != null)
            {
                final Element environment = document.createElement("environment");
                for (final Map.Entry<String, String> kvp : this.environment.entrySet())
                {
                    final Element envVar = document.createElement("var");
                    envVar.setAttribute("name", kvp.getKey().toUpperCase());
                    envVar.setAttribute("value", kvp.getValue());
                    environment.appendChild(envVar);
                }
                root.appendChild(environment);
            }

            // Query node
            final Element query = document.createElement("query");
            query.setTextContent(this.command);
            root.appendChild(query);

            // Context node
            if (this.context != null)
            {
                final Element context = document.createElement("context");
                for (final Map.Entry<String, String> kvp : this.context.entrySet())
                {
                    final Element contextField = document.createElement("field");
                    contextField.setAttribute("name", kvp.getKey());
                    contextField.setAttribute("oper", "EQ");
                    contextField.setAttribute("type", "STRING");
                    contextField.setTextContent(kvp.getValue());
                    context.appendChild(contextField);
                }
                root.appendChild(context);
            }

            // Convert to XML
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            final DOMSource domSource = new DOMSource(document);
            try (final StringWriter writer = new StringWriter())
            {
                final StreamResult streamResult = new StreamResult(writer);
                transformer.transform(domSource, streamResult);
                return writer.toString();
            }
        }
        catch (final ParserConfigurationException | TransformerException | IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
