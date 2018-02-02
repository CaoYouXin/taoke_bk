package com.taoke.miquaner.view;

import com.taoke.miquaner.data.EHelpDoc;

public class HelpDocSubmit extends EHelpDoc {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HelpDocSubmit{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
