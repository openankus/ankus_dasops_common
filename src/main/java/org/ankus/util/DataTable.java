package org.ankus.util;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class DataTable {
    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private String udate;

    private Page data;

    public Page getData(){
        return data;
    }
    public void setData(Page data){
        this.data = data;
    }

    private List list;
    public List getList(){
        return list;
    }
    public void setList(List list){
        this.list=list;
    }
}
