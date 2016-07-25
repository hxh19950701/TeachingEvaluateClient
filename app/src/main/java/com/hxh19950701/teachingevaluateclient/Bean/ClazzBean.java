package com.hxh19950701.teachingevaluateclient.Bean;

import java.util.List;

/**
 * Created by hxh19950701 on 2016/6/29.
 */
public class ClazzBean {

    /**
     * clazzList : [{"id":1,"mark":null,"name":"计科一班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":1,"mark":null,"name":"计算机科学"},"teacher":{"id":30,"mark":null,"name":"陈展望","sex":1,"teacherId":"10004"},"year":2013},{"id":2,"mark":null,"name":"计科二班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":1,"mark":null,"name":"计算机科学"},"teacher":{"id":30,"mark":null,"name":"陈展望","sex":1,"teacherId":"10004"},"year":2013},{"id":3,"mark":null,"name":"物联网三班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":15,"mark":null,"name":"校企合作班"},"teacher":null,"year":2013},{"id":4,"mark":null,"name":"网工四班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":3,"mark":null,"name":"网络工程"},"teacher":{"id":28,"mark":null,"name":"唐鹏举","sex":1,"teacherId":"10002"},"year":2013},{"id":5,"mark":null,"name":"软工五班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":2,"mark":null,"name":"软件工程"},"teacher":{"id":27,"mark":null,"name":"张显","sex":1,"teacherId":"10001"},"year":2013},{"id":6,"mark":null,"name":"软工六班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":2,"mark":null,"name":"软件工程"},"teacher":{"id":27,"mark":null,"name":"张显","sex":1,"teacherId":"10001"},"year":2013},{"id":7,"mark":null,"name":"计科一班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":1,"mark":null,"name":"计算机科学"},"teacher":null,"year":2012},{"id":8,"mark":null,"name":"计科二班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":1,"mark":null,"name":"计算机科学"},"teacher":null,"year":2012},{"id":9,"mark":null,"name":"IBM三班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":15,"mark":null,"name":"校企合作班"},"teacher":null,"year":2012},{"id":10,"mark":null,"name":"网工四班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":3,"mark":null,"name":"网络工程"},"teacher":null,"year":2012},{"id":11,"mark":null,"name":"软工五班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":2,"mark":null,"name":"软件工程"},"teacher":null,"year":2012},{"id":12,"mark":null,"name":"软工六班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":2,"mark":null,"name":"软件工程"},"teacher":null,"year":2012},{"id":13,"mark":null,"name":"软工七班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":2,"mark":null,"name":"软件工程"},"teacher":null,"year":2012},{"id":14,"mark":null,"name":"计科一班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":1,"mark":null,"name":"计算机科学"},"teacher":null,"year":2014},{"id":15,"mark":null,"name":"计科二班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":1,"mark":null,"name":"计算机科学"},"teacher":null,"year":2014},{"id":16,"mark":null,"name":"软件工程师三班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":15,"mark":null,"name":"校企合作班"},"teacher":null,"year":2014},{"id":17,"mark":null,"name":"网工四班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":3,"mark":null,"name":"网络工程"},"teacher":null,"year":2014},{"id":18,"mark":null,"name":"软工五班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":2,"mark":null,"name":"软件工程"},"teacher":null,"year":2014},{"id":19,"mark":null,"name":"小教一班","subject":{"department":{"id":2,"mark":null,"name":"教育科学系"},"id":4,"mark":null,"name":"小学教育"},"teacher":null,"year":2014},{"id":20,"mark":null,"name":"小教二班","subject":{"department":{"id":2,"mark":null,"name":"教育科学系"},"id":4,"mark":null,"name":"小学教育"},"teacher":null,"year":2014},{"id":21,"mark":null,"name":"学前三班","subject":{"department":{"id":2,"mark":null,"name":"教育科学系"},"id":5,"mark":null,"name":"学前教育"},"teacher":null,"year":2014},{"id":22,"mark":null,"name":"学前四班","subject":{"department":{"id":2,"mark":null,"name":"教育科学系"},"id":5,"mark":null,"name":"学前教育"},"teacher":null,"year":2014},{"id":23,"mark":null,"name":"人文五班","subject":{"department":{"id":2,"mark":null,"name":"教育科学系"},"id":6,"mark":null,"name":"人文教育"},"teacher":null,"year":2014},{"id":24,"mark":null,"name":"人文六班","subject":{"department":{"id":2,"mark":null,"name":"教育科学系"},"id":6,"mark":null,"name":"人文教育"},"teacher":null,"year":2014}]
     * success : true
     */

    private boolean success;
    /**
     * id : 1
     * mark : null
     * name : 计科一班
     * subject : {"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":1,"mark":null,"name":"计算机科学"}
     * teacher : {"id":30,"mark":null,"name":"陈展望","sex":1,"teacherId":"10004"}
     * year : 2013
     */

    private List<ClazzListBean> clazzList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ClazzListBean> getClazzList() {
        return clazzList;
    }

    public void setClazzList(List<ClazzListBean> clazzList) {
        this.clazzList = clazzList;
    }

    public static class ClazzListBean {
        private int id;
        private String mark;
        private String name;
        /**
         * department : {"id":1,"mark":null,"name":"计算机科学与技术系"}
         * id : 1
         * mark : null
         * name : 计算机科学
         */

        private SubjectBean subject;
        /**
         * id : 30
         * mark : null
         * name : 陈展望
         * sex : 1
         * teacherId : 10004
         */

        private TeacherBean teacher;
        private int year;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SubjectBean getSubject() {
            return subject;
        }

        public void setSubject(SubjectBean subject) {
            this.subject = subject;
        }

        public TeacherBean getTeacher() {
            return teacher;
        }

        public void setTeacher(TeacherBean teacher) {
            this.teacher = teacher;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public static class SubjectBean {
            /**
             * id : 1
             * mark : null
             * name : 计算机科学与技术系
             */

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

            public static class DepartmentBean {
                private int id;
                private Object mark;
                private String name;

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

        public static class TeacherBean {
            private int id;
            private Object mark;
            private String name;
            private int sex;
            private String teacherId;

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

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getTeacherId() {
                return teacherId;
            }

            public void setTeacherId(String teacherId) {
                this.teacherId = teacherId;
            }
        }
    }
}
