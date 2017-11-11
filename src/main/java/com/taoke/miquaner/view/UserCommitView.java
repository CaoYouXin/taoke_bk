package com.taoke.miquaner.view;

public class UserCommitView {

    private String name;
    private String commit;

    public UserCommitView(String name, String commit) {
        this.name = name;
        this.commit = commit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }
}
