package com.taoke.miquaner.data;

import javax.persistence.*;

@Entity
@Table(name = "help")
public class EHelp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question", unique = true, nullable = false)
    private String question;

    @Column(name = "answer", nullable = false, length = 10240)
    private String answer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
