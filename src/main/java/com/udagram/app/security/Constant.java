package com.udagram.app.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constant {
	public static final String JWT_HEADER_NAME="Authorization";
    public static final String SECRET="qdsfqdfqdfqdf";
    public static final long EXPIRATION=10*24*3600;
    public static final String HEADER_PREFIX="Bearer ";
    public static final List<String> ROLE_LIST=new ArrayList<>(Arrays.asList("UDAGRAM_USER"));
}
