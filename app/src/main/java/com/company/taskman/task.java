package com.company.taskman;

public class task {
    //Модель данных полета
    private String theme,description,status;
    private String id;

    public task(){

    }

    public task(String theme, String desc, String status, String id) {
        this.theme = theme;
        this.description = desc;
        this.status = status;
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
