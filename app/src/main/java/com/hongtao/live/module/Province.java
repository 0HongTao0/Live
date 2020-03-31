package com.hongtao.live.module;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Created 2020/3/30.
 *
 * @author HongTao
 */
public class Province implements IPickerViewData {
    /**
     * name : 北京市
     * id : 1
     */

    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
