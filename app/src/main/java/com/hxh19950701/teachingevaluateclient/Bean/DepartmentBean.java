package com.hxh19950701.teachingevaluateclient.Bean;

import java.util.List;

/**
 * Created by hxh19950701 on 2016/6/29.
 */
public class DepartmentBean {

    private boolean success;

    private List<DepartmentListBean> departmentList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DepartmentListBean> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<DepartmentListBean> departmentList) {
        this.departmentList = departmentList;
    }

    public static class DepartmentListBean {
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
