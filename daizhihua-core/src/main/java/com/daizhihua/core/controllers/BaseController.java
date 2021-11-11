package com.daizhihua.core.controllers;

import com.daizhihua.core.res.Resut;

public interface BaseController<T> {

//    Resut list();

    Resut add(T t);

    Resut delete(Long id);

    Resut update(T t);

}
