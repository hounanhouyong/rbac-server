package com.hn.rbac.server.web.system.model;

import com.hn.rbac.server.share.model.Menu;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class MenuTree<T> implements Serializable {
    private static final long serialVersionUID = 6800436325255232858L;
    private String id;
    private String parentId;
    private String menuName;
    private String icon;
    private String url;
    private Map<String, Object> state;
    private boolean checked = false;
    private Map<String, Object> attributes;
    private List<MenuTree<T>> childs = new ArrayList<>();

    private boolean hasParent = false;
    private boolean hasChild = false;

    private Menu data;

}
