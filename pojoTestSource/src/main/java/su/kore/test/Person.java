package su.kore.test;

import su.kore.tools.repojo.Exclude;
import su.kore.tools.repojo.Generate;
import su.kore.tools.repojo.GenerateList;
import su.kore.tools.repojo.MetaClass;

import java.util.List;

/**
 * Created by krad on 13.04.2017.
 */
@MetaClass
@GenerateList({
        @Generate(target = "so", generatorClass = "su.kore.tools.repojo.generators.PojoGenerator",
                packageName = "su.kore.test.sos", suffix = ""),
        @Generate(target = "do", generatorClass = "su.kore.tools.repojo.generators.PojoWithBuilderGenerator",
                packageName = "su.kore.test.dos", suffix = "DO")
})
public class Person {
    private String name;
    private String surname;
    private Integer age;
    private Boolean human;
    private List<Person> children;
    private Person mate;
    private Address address;
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

    public List<Person> getChildren() {
        return children;
    }

    public void setChildren(List<Person> children) {
        this.children = children;
    }

    @Exclude({"so", "do"})
    public boolean isPermanent() {
        return permanent;
    }

    public Person getMate() {
        return mate;
    }

    public void setMate(Person mate) {
        this.mate = mate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
