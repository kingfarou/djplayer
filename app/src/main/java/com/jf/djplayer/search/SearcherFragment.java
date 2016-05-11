package com.jf.djplayer.search;

/**
 * Created by JF on 2016/5/11.
 * "Fragment"实现该接口而成为具备搜索功能的"Fragment"
 * 接收关键字并刷新自身信息
 */
public interface SearcherFragment {

    /**
     * 根据关键字来进行搜索
     * @param keyword 关键字
     */
    public void search(String keyword);
}
