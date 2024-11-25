package com.Assignment;

class Item {
    private String name;
    private Integer id;
    private Integer listId;

    public Item(int id, int listId, String name) {
        this.id = id;
        this.listId = listId;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

}
