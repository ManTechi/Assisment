package com.jspider.RoxilerApp.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStructure<T>{
	private String message;
	private T data;
	private int status; 
///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
}
