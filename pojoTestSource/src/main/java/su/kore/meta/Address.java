package su.kore.meta;

import su.kore.tools.repojo.Generate;
import su.kore.tools.repojo.GenerateList;
import su.kore.tools.repojo.MetaClass;
import su.kore.tools.repojo.generators.PojoGenerator;
import su.kore.tools.repojo.generators.PojoWithBuilderGenerator;

/**
 * Created by krad on 20.06.2017.
 */
@MetaClass
@GenerateList({
        @Generate(target = "so", generatorClass = PojoGenerator.class,
                packageName = "su.kore.test.sos", suffix = ""),
        @Generate(target = "do", generatorClass = PojoWithBuilderGenerator.class,
                packageName = "su.kore.test.dos", suffix = "DOm")
})
class Address {
    private String street;
    private Integer house;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouse() {
        return house;
    }

    public void setHouse(Integer house) {
        this.house = house;
    }
}
