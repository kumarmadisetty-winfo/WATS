package com.winfo.vo;

public class GraphqlRequestBody {

	private String query;
	private Object variables;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Object getVariables() {
		return variables;
	}

	public void setVariables(Object variables) {
		this.variables = variables;
	}

	public GraphqlRequestBody(String query, Object variables) {
		super();
		this.query = query;
		this.variables = variables;
	}

	public GraphqlRequestBody() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}