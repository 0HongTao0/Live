package com.hongtao.live.module;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * Created 2020/3/30.
 *
 * @author HongTao
 */
public class City implements IPickerViewData {
    /**
     * name : 广州市
     * id : 243
     * provinceId : 19
     */

    private String name;
    private int id;
    private int provinceId;

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

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
