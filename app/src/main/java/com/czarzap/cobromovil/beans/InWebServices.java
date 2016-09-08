package com.czarzap.cobromovil.beans;

/**
 * Created by Alfredo on 11/07/2016.
 */
public class InWebServices {
    InWebServicesPK id = new InWebServicesPK();
    String ws_url;

    public InWebServices(Integer ws_empresa, Integer ws_id,String ws_url) {
        super();
        this.id.ws_empresa = ws_empresa;
        this.id.ws_id = ws_id;
        this.ws_url = ws_url;
    }


    public InWebServicesPK getId() { return id; }
    public void setId(InWebServicesPK id) { this.id = id; }
    public Integer getWs_empresa() { return id.ws_empresa; }
    public void setWs_empresa(Integer ws_empresa) { this.id.ws_empresa = ws_empresa; }
    public Integer getWs_id() { return id.ws_id; }
    public void setWs_id(Integer ws_id) { this.id.ws_id = ws_id; }
    public String getWs_url() { return ws_url; }
    public void setWs_url(String ws_url) { this.ws_url = ws_url; }

    @Override
    public String toString() {
        return "InWebServices [id=" + id.toString() + ", ws_url=" + ws_url + "]";
    }
}


class InWebServicesPK  {
    private static final long serialVersionUID = 1L;
    Integer ws_empresa;
    Integer ws_id;


    public Integer getWs_empresa() { return ws_empresa; }
    public void setWs_empresa(Integer ws_empresa) { this.ws_empresa = ws_empresa; }
    public Integer getWs_id() { return ws_id; }
    public void setWs_id(Integer ws_id) { this.ws_id = ws_id; }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ws_empresa == null) ? 0 : ws_empresa.hashCode());
        result = prime * result + ((ws_id == null) ? 0 : ws_id.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InWebServicesPK other = (InWebServicesPK) obj;
        if (ws_empresa == null) {
            if (other.ws_empresa != null)
                return false;
        } else if (!ws_empresa.equals(other.ws_empresa))
            return false;
        if (ws_id == null) {
            if (other.ws_id != null)
                return false;
        } else if (!ws_id.equals(other.ws_id))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "InWebServicesPK [ws_empresa=" + ws_empresa + ", ws_id=" + ws_id + "]";
    }

}
