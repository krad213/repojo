package su.kore.test;

import su.kore.tools.repojo.Exclude;
import su.kore.tools.repojo.Generate;
import su.kore.tools.repojo.Pojo;
import su.kore.tools.repojo.TargetType;

import java.util.List;

/**
 * Created by krad on 13.04.2017.
 */
@Pojo
@Generate(id = "so", targetType = TargetType.POJO, packageName = "su.kore.test.so", suffix = "SO")
public class Person {
    private String name;
    private String surname;
    private Integer age;
    private Boolean human;
    private Gender gender;
    private List<Person> children;
    private boolean permanent = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getHuman() {
        return human;
    }

    public void setHuman(Boolean human) {
        this.human = human;
    }

    @Exclude(id = "so")
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


    public List<Person> getChildren() {
        return children;
    }

    public void setChildren(List<Person> children) {
        this.children = children;
    }

    public boolean isPermanent() {
        return permanent;
    }
}
