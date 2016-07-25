package com.hxh19950701.teachingevaluateclient.Bean;

import java.util.List;

/**
 * Created by hxh19950701 on 2016/6/29.
 */
public class SubjectBean {

    /**
     * success : true
     * subjectList : [{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":1,"mark":null,"name":"计算机科学"},{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":2,"mark":null,"name":"软件工程"},{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":3,"mark":null,"name":"网络工程"},{"department":{"id":2,"mark":null,"name":"教育科学系"},"id":4,"mark":null,"name":"小学教育"},{"department":{"id":2,"mark":null,"name":"教育科学系"},"id":5,"mark":null,"name":"学前教育"},{"department":{"id":2,"mark":null,"name":"教育科学系"},"id":6,"mark":null,"name":"人文教育"},{"department":{"id":3,"mark":null,"name":"外国语言文学系"},"id":7,"mark":null,"name":"商务英语"},{"department":{"id":3,"mark":null,"name":"外国语言文学系"},"id":8,"mark":null,"name":"师范英语"},{"department":{"id":4,"mark":null,"name":"商学系"},"id":9,"mark":null,"name":"酒店管理"},{"department":{"id":4,"mark":null,"name":"商学系"},"id":10,"mark":null,"name":"物流管理"},{"department":{"id":4,"mark":null,"name":"商学系"},"id":11,"mark":null,"name":"财务管理"},{"department":{"id":4,"mark":null,"name":"商学系"},"id":12,"mark":null,"name":"旅游管理"},{"department":{"id":5,"mark":null,"name":"中国语言文学系"},"id":13,"mark":null,"name":"汉语言文学"},{"department":{"id":5,"mark":null,"name":"中国语言文学系"},"id":14,"mark":null,"name":"广播电视与文学"},{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":15,"mark":null,"name":"校企合作班"}]
     */

    private boolean success;
    /**
     * department : {"id":1,"mark":null,"name":"计算机科学与技术系"}
     * id : 1
     * mark : null
     * name : 计算机科学
     */

    private List<SubjectListBean> subjectList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<SubjectListBean> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<SubjectListBean> subjectList) {
        this.subjectList = subjectList;
    }

    public static class SubjectListBean {

        private DepartmentBean department;
        private int id;
        private Object mark;
        private String name;

        public DepartmentBean getDepartment() {
            return department;
        }

        public void setDepartment(DepartmentBean department) {
            this.department = department;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getMark() {
            return mark;
        }

        public void setMark(Object mark) {
            this.mark = mark;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
