package su.kore.test;

import java.util.List;

import su.kore.tools.repojo.Exclude;
import su.kore.tools.repojo.Generate;
import su.kore.tools.repojo.GenerateList;
import su.kore.tools.repojo.Pojo;

/**
 * Created by krad on 13.04.2017.
 */
@Pojo
@GenerateList({
        @Generate(target = "so", generatorClass = "su.kore.tools.repojo.generators.PojoGenerator",
                packageName = "su.kore.test.sos", suffix = "SO"),
        @Generate(target = "do", generatorClass = "su.kore.tools.repojo.generators.PojoWithBuilderGenerator",
                packageName = "su.kore.test.dos", suffix = "DO")
})
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

    @Exclude(target = "so")
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
