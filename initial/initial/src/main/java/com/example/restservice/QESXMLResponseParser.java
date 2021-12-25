package com.example.restservice;

import org.apache.solr.client.solrj.impl.XMLResponseParser;

class QESXMLResponseParser extends XMLResponseParser {
    public QESXMLResponseParser() {
        super();
    }

    public String getContentType() {
        return "text/xml; charset=UTF-8";
    }
}
