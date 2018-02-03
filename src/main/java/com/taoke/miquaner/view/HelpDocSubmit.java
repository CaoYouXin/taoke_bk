package com.taoke.miquaner.view;

import com.taoke.miquaner.data.EHelpDoc;

public class HelpDocSubmit extends EHelpDoc {

    private String fileName;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "HelpDocSubmit{" +
                "fileName='" + fileName + '\'' +
                ", content='" + content + '\'' +
                "} " + super.toString();
    }
}
