package com.jkzz.demo;

import java.util.HashMap;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2018-12-24 10:21
 **/
public class SubjectT extends HashMap<String, Object> {
    private Long memberId = null;
    private String sessionId = null;
    public static final ThreadLocal<SubjectT> tl = new ThreadLocal();

    public SubjectT() {
    }

    public static SubjectT getSubject() {
           SubjectT  subject = (SubjectT)tl.get();
        if (subject == null) {
            subject = new SubjectT();
            tl.set(subject);
        }

        return subject;
    }
}
