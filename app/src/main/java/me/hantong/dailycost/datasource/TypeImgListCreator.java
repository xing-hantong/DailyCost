package me.hantong.dailycost.datasource;

import java.util.ArrayList;
import java.util.List;

import me.hantong.dailycost.database.entity.RecordType;
import me.hantong.dailycost.ui.addtype.TypeImgBean;


/**
 * 产生类型图片数据
 *
 * @author X
 */
public class TypeImgListCreator {

    public static List<TypeImgBean> createTypeImgBeanData(int type) {

        List<TypeImgBean> list = new ArrayList<>();
        TypeImgBean bean;

        if (type == RecordType.TYPE_OUTLAY) {
            bean = new TypeImgBean("type_eat");
            list.add(bean);

            bean = new TypeImgBean("type_calendar");
            list.add(bean);

            bean = new TypeImgBean("type_3c");
            list.add(bean);

            bean = new TypeImgBean("type_clothes");
            list.add(bean);

            bean = new TypeImgBean("type_candy");
            list.add(bean);

            bean = new TypeImgBean("type_cigarette");
            list.add(bean);

            bean = new TypeImgBean("type_humanity");
            list.add(bean);

            bean = new TypeImgBean("type_pill");
            list.add(bean);

            bean = new TypeImgBean("type_fitness");
            list.add(bean);

            bean = new TypeImgBean("type_sim");
            list.add(bean);

            bean = new TypeImgBean("type_study");
            list.add(bean);

            bean = new TypeImgBean("type_pet");
            list.add(bean);

            bean = new TypeImgBean("type_train");
            list.add(bean);
        } else {
            bean = new TypeImgBean("type_salary");
            list.add(bean);

            bean = new TypeImgBean("type_pluralism");
            list.add(bean);
        }
        return list;
    }

}
