package com.jf.djplayer.search;

import java.util.List;

/**
 * Created by JF on 2016/5/11.
 * 搜索--带搜索数据提供者
 * 任何持有待搜索数据的对象，实现该接口就可以对外暴露待搜索的数据
 */
public interface SearchedDataProvider {

    /**
     * 返回待搜索的数据列表
     * @return 待搜索的数据列表
     */
    public List returnSearchedDataList();
}
