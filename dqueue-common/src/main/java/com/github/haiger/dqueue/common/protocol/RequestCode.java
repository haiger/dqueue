package com.github.haiger.dqueue.common.protocol;

/**
 * @author haiger
 * @since 2017年1月7日 上午12:45:24
 */
public enum RequestCode {
    PUB("pub"), MPUB("mpub"), FIN("fin"), DEL("del"), 
    SUB("sub"), POP("pop"), MPOP("mpop");
    
    private String code;

    private RequestCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
