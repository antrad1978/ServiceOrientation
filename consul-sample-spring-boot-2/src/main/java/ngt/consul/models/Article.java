package ngt.consul.models;

public class Article {
    private String name;
    private String description;


    public Article(String name, String className) {
        super();
        this.name = name;
        this.description = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
