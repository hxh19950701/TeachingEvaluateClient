package com.hxh19950701.teachingevaluateclient.Bean;

/**
 * Created by hxh19950701 on 2016/6/8.
 */
public class StudentInfoBean {

    /**
     * clazz : {"id":5,"mark":null,"name":"软工五班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":2,"mark":null,"name":"软件工程"},"teacher":{"id":27,"mark":null,"name":"张显","sex":1,"teacherId":"10001"},"year":2013}
     * id : 14
     * mark : null
     * name : 胡绪浩
     * sex : 1
     * studentId : 1306405027
     */

    private StudentBean student;
    /**
     * student : {"clazz":{"id":5,"mark":null,"name":"软工五班","subject":{"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":2,"mark":null,"name":"软件工程"},"teacher":{"id":27,"mark":null,"name":"张显","sex":1,"teacherId":"10001"},"year":2013},"id":14,"mark":null,"name":"胡绪浩","sex":1,"studentId":"1306405027"}
     * success : true
     */

    private boolean success;

    public StudentBean getStudent() {
        return student;
    }

    public void setStudent(StudentBean student) {
        this.student = student;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class StudentBean {
        /**
         * id : 5
         * mark : null
         * name : 软工五班
         * subject : {"department":{"id":1,"mark":null,"name":"计算机科学与技术系"},"id":2,"mark":null,"name":"软件工程"}
         * teacher : {"id":27,"mark":null,"name":"张显","sex":1,"teacherId":"10001"}
         * year : 2013
         */

        private ClazzBean clazz;
        private int id;
        private Object mark;
        private String name;
        private int sex;
        private String studentId;

        public ClazzBean getClazz() {
            return clazz;
        }

        public void setClazz(ClazzBean clazz) {
            this.clazz = clazz;
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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public static class ClazzBean {
            private int id;
            private Object mark;
            private String name;
            /**
             * department : {"id":1,"mark":null,"name":"计算机科学与技术系"}
             * id : 2
             * mark : null
             * name : 软件工程
             */

            private SubjectBean subject;
            /**
             * id : 27
             * mark : null
             * name : 张显
             * sex : 1
             * teacherId : 10001
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

            public void setMark(Object mark) {
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
}
