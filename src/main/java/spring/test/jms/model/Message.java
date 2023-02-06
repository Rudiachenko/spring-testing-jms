package spring.test.jms.model;

public interface Message {
    long getId();

    void setId(long id);

    String getContent();

    void setContent(String content);
}
