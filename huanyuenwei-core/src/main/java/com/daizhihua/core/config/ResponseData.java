package com.daizhihua.core.config;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseData {

    private String message;

    private int status;

    private Object object;
    public ResponseData() {
    }

    public ResponseData(Object object) {
        this.message="ok";
        this.status= HttpStatus.OK.value();
        this.object = object;
    }

    public ResponseData result(Object object) {
        return new ResponseData(object);
    }

    public ResponseData(boolean flag){
        if(flag){
            this.message="ok";
            this.status= HttpStatus.OK.value();
        }else{
            this.status=HttpStatus.CREATED.value();
            this.message="fild";

        }
    }

    public ResponseData(int code,String message){
        this.message = message;
        this.status = code;
    }


}
