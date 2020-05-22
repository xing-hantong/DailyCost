package me.hantong.dailycost.ui.setting;

/**
 * @author X
 * @date 2020/5/22
 */
public class OpenSourceBean {
    public OpenSourceBean(String title, String url, String license) {
        this.title = title;
        this.url = url;
        this.license = license;
    }

    public String title;
    public String url;
    public String license;
}
