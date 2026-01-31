package com.spring.what.security.adapter;

import java.util.List;

public interface AuthAdapter {

    List<String> pathPatterns();


    List<String> excludePathPaterns();

}
